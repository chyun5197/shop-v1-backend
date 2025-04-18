package project.shopclone.domain.order.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.order.dto.VerificationResult;
import project.shopclone.domain.order.dto.request.OrderCompleteRequest;
import project.shopclone.domain.order.dto.request.OrderPrepareRequest;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;
import project.shopclone.domain.order.exception.OrderErrorCode;
import project.shopclone.domain.order.exception.OrderException;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.order.repository.PaymentRepository;
import project.shopclone.domain.order.service.OrderService;
import project.shopclone.domain.order.service.PaymentService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "결제 API")
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Operation(summary = "결제금액 사전등록")
    @PostMapping("/prepare")
    public ResponseEntity<String> prepareOrder(@RequestHeader("Authorization") String token,
                                                             @RequestBody OrderPrepareRequest prepareRequest) throws IamportResponseException, IOException {
        String merchantId = paymentService.prepare(token, prepareRequest);
        return ResponseEntity.ok(merchantId);
    }

    @Operation(summary = "결제 검증 후 결제 승인/실패 처리")
    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestHeader("Authorization") String token,
                                                              @RequestBody OrderCompleteRequest orderCompleteRequest) throws IamportResponseException, IOException {

        // 주문서, 결제내역, 결제 고유번호, 주문번호 초기화
        Orders orderSheet = orderRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
        Payment payment = paymentRepository.findByMerchantUid(orderCompleteRequest.getMerchantUid()).orElseThrow();
        String impUid = orderCompleteRequest.getImpUid();
        String merchantUid = payment.getMerchantUid();

        // 결제 검증
        VerificationResult verificationResult = paymentService.postVerification(payment, impUid);

        // 결제 승인
        if (verificationResult.isCorrect()) {
            // 상품 재고 감소 (중복 구매 충돌 확인후)
            for(OrderItem orderItem : orderSheet.getOrderItemList()){
                orderService.redissonDecreaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
            }
//                orderService.removeStock(orderSheet);

            // 주문 완료, 결제 완료
            paymentService.completeOrderAndPayment(orderSheet, payment, verificationResult, impUid);
            // 주문 완료 후처리: 장바구니 상품 삭제, 적립금 업데이트, 알림 전송
            return ResponseEntity.ok("complete");
        }
        // 결제 실패
        else{
            // 결제 취소 요청
            paymentService.cancelPayment(payment, merchantUid, false);
            return ResponseEntity.ok("fail");
        }
    }

    @Operation(summary = "환불하기")
    @PostMapping("/cancel/{merchantUid}")
    public ResponseEntity<String> cancelOrder(@RequestHeader("Authorization") String token,
                                              @PathVariable String merchantUid) throws IOException, IamportResponseException {
        return paymentService.cancelOrder(merchantUid);
    }

}
