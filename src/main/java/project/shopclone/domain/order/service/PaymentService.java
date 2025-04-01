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
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberService;
import project.shopclone.domain.order.dto.request.OrderCompleteRequest;
import project.shopclone.domain.order.dto.request.OrderItemRequest;
import project.shopclone.domain.order.dto.request.OrderPrepareRequest;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;
import project.shopclone.domain.order.repository.OrderItemRepository;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.order.repository.PaymentRepository;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
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
    @Transactional
    public String prepare(String token, OrderPrepareRequest prepareRequest) throws IamportResponseException, IOException {
        Member member = memberService.getMember(token);
        String mid = prepareRequest.getMerchantUid(); // 쇼핑몰 주문번호

        // 기존에 생성한 주문정보가 있으면 패스
        if (orderRepository.findByMerchantUid(mid).isPresent()){
            return "fail";
        }

        // 주문정보 테이블 생성
        Orders order = orderRepository.save(Orders.builder()
                .member(member)
                .merchantUid(mid)
                .orderName(prepareRequest.getOrderName())
                .orderPhone(prepareRequest.getPhone())
                .orderEmail(prepareRequest.getEmail())
                .paymentStatus(false)
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

        // 포트원 결제금액 사전등록 (포트원 토큰은 메서드 내부에서 응답 받은걸로 포함하여 요청)
        IamportResponse<Prepare> prepareIamportResponse = iamportClient.postPrepare(new PrepareData(mid, new BigDecimal(1000)));// 모의 결제이므로 금액 1000원으로 설정

        // 사전등록 성공 시 결제정보 Payment 엔티티 저장
        if (prepareIamportResponse.getCode() == 0){ // 0일때가 ok
            paymentRepository.save(Payment.builder()
                    .orders(order)
                    .merchantUid(mid)
                    .realPrice(totalPrice)
                    .paidPrice(1000)
                    .totalQuantity(totalQuantity)
                    .paymentStatus(false)
                    .build());
        }else{
            log.info("오류 메세지: {}", prepareIamportResponse.getMessage());
        }

        return order.getMerchantUid();
    }

    // 결제 검증 및 결제 완료 처리
    public ResponseEntity<String> completeOrder(String token, OrderCompleteRequest orderCompleteRequest) throws IamportResponseException, IOException {
        Member member = memberService.getMember(token);
        Payment payment = paymentRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid()).orElseThrow();
        Orders order = orderRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid()).orElseThrow();
        String impUid = orderCompleteRequest.getImpUid();

        // 사전등록 결제정보 보기
        IamportResponse<Prepare> getPrepareResponse = iamportClient.getPrepare(orderCompleteRequest.getMerchantUid());
        System.out.println("getPrepareResponse.getResponse() = " + getPrepareResponse.getResponse());

        // 결제 검증
        // 사전 등록 결제금액 조회
        IamportResponse<com.siot.IamportRestClient.response.Payment> paymentIamportResponse = iamportClient.paymentByImpUid(impUid);
        if (!Objects.equals(paymentIamportResponse.getResponse().getAmount(), new BigDecimal(payment.getPaidPrice()))){
            // 결제 취소 요청 (두번째 파라미터가 포트원 고유번호로 취소 요청)
            IamportResponse<com.siot.IamportRestClient.response.Payment> cancelPaymentByImpUid = iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
            log.info("결제 취소 응답(message): {}", cancelPaymentByImpUid.getMessage());
            return ResponseEntity.ok("cancel");
        }
        // 결제 승인 - 주문 완료, 결제 완료
        order.setPaymentStatus(true);
        payment.setPaidAt(LocalDateTime.now());
        payment.setPaymentStatus(true);

        return ResponseEntity.ok("complete");
    }
}
