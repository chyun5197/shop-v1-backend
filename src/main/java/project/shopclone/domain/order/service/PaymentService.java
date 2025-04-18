package project.shopclone.domain.order.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.order.dto.VerificationResult;
import project.shopclone.domain.order.dto.request.OrderCompleteRequest;
import project.shopclone.domain.order.dto.request.OrderItemRequest;
import project.shopclone.domain.order.dto.request.OrderPrepareRequest;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;
import project.shopclone.domain.order.entity.PaymentStatus;
import project.shopclone.domain.order.exception.OrderErrorCode;
import project.shopclone.domain.order.exception.OrderException;
import project.shopclone.domain.order.repository.OrderItemRepository;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.order.repository.PaymentRepository;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private IamportClient iamportClient;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    @Value("${data.iamport.key}")
    private String apiKey;

    @Value("${data.iamport.secret}")
    private String apiSecret;


    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    // 결제금액 사전등록
    public String prepare(String token, OrderPrepareRequest prepareRequest) throws IamportResponseException, IOException {
        Member member = memberService.getMember(token);
        String mid = prepareRequest.getMerchantUid(); // 쇼핑몰 주문번호

        // 기존에 생성한 주문정보 및 사전등록이 있으면 아래 절차 생략
        if (orderRepository.findByMerchantUid(mid).isPresent()){
            return "pass";
        }

        // 주문정보 테이블 생성
        Orders order = orderRepository.save(Orders.builder()
                .member(member)
                .merchantUid(mid)
                .orderName(prepareRequest.getOrderName())
                .orderEmail(prepareRequest.getEmail())
                .isPaid(false)
                .build());

        Integer totalQuantity = 0;
        int totalPrice = 0;

        // 주문 아이템 테이블 생성
        List<OrderItemRequest> orderItemList = prepareRequest.getOrderSheetItems();
        for(OrderItemRequest orderItemRequest : orderItemList ){
            Product product = productRepository.findById(orderItemRequest.getProductId()).orElseThrow();
            orderItemRepository.save(OrderItem.builder()
                    .orders(order)
                    .quantity(orderItemRequest.getQuantity())
                    .product(product)
                    .build());
            totalQuantity += orderItemRequest.getQuantity();
            totalPrice += product.getPrice() * orderItemRequest.getQuantity();
        }
        order.updateTotalQuantity(totalQuantity);
        order.updateTotalPrice(totalPrice);

        // 포트원 결제금액 사전등록 (API: POST /payments/prepare) (포트원 토큰은 메서드 내부에서 응답 받은걸로 포함하여 요청)
        // 모의 결제이므로 금액 1000원으로 고정
        IamportResponse<Prepare> prepareIamportResponse = iamportClient.postPrepare(new PrepareData(mid, new BigDecimal(1000)));

        // 사전등록 성공 시 결제정보 Payment 테이블 생성
        if (prepareIamportResponse.getCode() == 0){ // 0일때가 ok
            paymentRepository.save(Payment.builder()
                            .orders(order)
                            .merchantUid(mid)
                            .realPrice(totalPrice)
                            .paidPrice(1000)
                            .totalQuantity(totalQuantity)
                            .paymentStatus(PaymentStatus.READY)
                            .orderCompleted(false)
                    .build());
        }else{
            log.info("오류 메세지: {}", prepareIamportResponse.getMessage());
        }

        return order.getMerchantUid();
    }

    // 결제 금액 사후 검증 - DB 결제 금액과 실제 결제 금액 비교
    public VerificationResult postVerification(Payment payment, String impUid) throws IamportResponseException, IOException {
        // 포트원 결제내역 조회 (API: GET /payments/{imp_uid})
        com.siot.IamportRestClient.response.Payment portOneResponse = iamportClient.paymentByImpUid(impUid).getResponse();

        // 다르면 결제 취소 요청
        if (!Objects.equals(portOneResponse.getAmount(), new BigDecimal(payment.getPaidPrice()))){
            cancelPayment(payment, impUid, true);
            return new VerificationResult(false, portOneResponse);
        }
        return new VerificationResult(true, portOneResponse);
    }

    // 주문 완료, 결제 완료
    public void completeOrderAndPayment(Orders orderSheet, Payment payment, VerificationResult verificationResult, String impUid){
        orderSheet.updateIsPaid(true); // 주문상태 지불 완료
        payment.paymentSuccess(verificationResult.getPortOneResponse(), impUid); // 결제 수정 - 결제 상태, 결제 방법, 결제 일자, 구매자명, 구매자 이메일, impUid 저장
    }

    // 환불하기 (주문완료 이후)
    public ResponseEntity<String> cancelOrder(String merchantUid) throws IOException, IamportResponseException {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new OrderException(OrderErrorCode.PAYMENT_NOT_FOUND));
        if(payment.getPaymentStatus() == PaymentStatus.CANCEL){
            return ResponseEntity.ok("already canceled");
        }
        // 결제 취소 요청
        cancelPayment(payment, merchantUid, false);
        return ResponseEntity.ok("cancel");
    }

    // 포트원에 결제 취소 요청 (API: POST /payments/cancel)
    public void cancelPayment(Payment payment, String uid, boolean uidType) throws IOException, IamportResponseException {
        /*
        uidType: true - impUid / false - merchantUid
        true가 포트원 고유번호로 취소 요청 / false가 쇼핑몰 주문번호로 취소 요청
        */
        IamportResponse<com.siot.IamportRestClient.response.Payment> cancelPaymentByImpUid
                = iamportClient.cancelPaymentByImpUid(new CancelData(uid, uidType));

        log.info("결제 취소 응답(message): {}", cancelPaymentByImpUid.getMessage());
        log.info("결제 취소 응답(code): {}", cancelPaymentByImpUid.getCode());
        payment.setPaymentStatus(PaymentStatus.CANCEL);
    }


    // 결제 검증 및 결제 완료 처리 - 과정 나누기 전
//    public ResponseEntity<String> complete(String token, OrderCompleteRequest orderCompleteRequest) throws IamportResponseException, IOException {
//        Payment payment = paymentRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid()).orElseThrow();
//        Orders order = orderRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid())
//                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
//        String impUid = orderCompleteRequest.getImpUid();
//
//        /*
//        * (결제금액 사후 검증) DB 결제 금액과 실제 결제 금액 비교
//        */
//        // 포트원 결제내역 조회 (API: GET /payments/{imp_uid})
//        com.siot.IamportRestClient.response.Payment portOneResponse = iamportClient.paymentByImpUid(impUid).getResponse();
//        // 다르면 결제 취소 요청 (API: POST /payments/cancel) (두번째 파라미터 true가 포트원 고유번호로 취소 요청)
//        if (!Objects.equals(portOneResponse.getAmount(), new BigDecimal(payment.getPaidPrice()))){
//            IamportResponse<com.siot.IamportRestClient.response.Payment> cancelPaymentByImpUid = iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
//            log.info("결제 취소 응답(message): {}", cancelPaymentByImpUid.getMessage());
//            payment.setPaymentStatus(PaymentStatus.CANCEL);
//            return ResponseEntity.ok("fail");
//        }
//        // 결제 승인 - 주문 완료, 결제 완료
//        order.setIsPaid(true); // 주문상태 지불 완료
//        payment.paymentSuccess(portOneResponse, impUid); // 결제 수정 - 결제 상태, 결제 방법, 결제 일자, 구매자명, 구매자 이메일, impUid 저장
//
//        return ResponseEntity.ok("complete");
//    }
}
