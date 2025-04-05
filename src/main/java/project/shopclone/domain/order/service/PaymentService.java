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
        order.setTotalQuantity(totalQuantity);
        order.setTotalPrice(totalPrice);

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

    // 결제 검증 및 결제 완료 처리
    public ResponseEntity<String> completeOrder(String token, OrderCompleteRequest orderCompleteRequest) throws IamportResponseException, IOException {
        Payment payment = paymentRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid()).orElseThrow();
        Orders order = orderRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
        String impUid = orderCompleteRequest.getImpUid();

        // 포트원 결제내역 조회 (API: GET /payments/{imp_uid})
        com.siot.IamportRestClient.response.Payment portOneResponse = iamportClient.paymentByImpUid(impUid).getResponse();

        // 결제 금액 비교
        if (!Objects.equals(portOneResponse.getAmount(), new BigDecimal(payment.getPaidPrice()))){
            // 결제 취소 요청 (API: POST /payments/cancel) (두번째 파라미터 true가 포트원 고유번호로 취소 요청)
            IamportResponse<com.siot.IamportRestClient.response.Payment> cancelPaymentByImpUid = iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
            log.info("결제 취소 응답(message): {}", cancelPaymentByImpUid.getMessage());
            payment.setPaymentStatus(PaymentStatus.CANCEL);
            return ResponseEntity.ok("cancel");
        }
        // 결제 승인 - 주문 완료, 결제 완료
        order.setIsPaid(true); // 주문상태 지불 완료
        payment.paymentSuccess(portOneResponse, impUid); // 결제 수정 - 결제 상태, 결제 방법, 결제 일자, 구매자명, 구매자 이메일, impUid 저장
        
        return ResponseEntity.ok("complete");
    }

    // 환불하기
    public ResponseEntity<String> cancelOrder(String token, String merchantUid) throws IOException, IamportResponseException {
        Payment payment = paymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new OrderException(OrderErrorCode.PAYMENT_NOT_FOUND));
        if(payment.getPaymentStatus() == PaymentStatus.CANCEL){
            return ResponseEntity.ok("already canceled");
        }

        // 결제 취소 요청 (API: POST /payments/cancel) (두번째 파라미터 false가 쇼핑몰 주문번호로 취소 요청).
        IamportResponse<com.siot.IamportRestClient.response.Payment> cancelPaymentByImpUid = iamportClient.cancelPaymentByImpUid(new CancelData(merchantUid, false));
        log.info("결제 취소 응답(message): {}", cancelPaymentByImpUid.getMessage());
        log.info("결제 취소 응답(code): {}", cancelPaymentByImpUid.getCode());
        payment.setPaymentStatus(PaymentStatus.CANCEL);

        return ResponseEntity.ok("cancel");
    }
}
