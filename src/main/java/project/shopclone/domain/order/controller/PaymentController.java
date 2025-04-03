package project.shopclone.domain.order.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.order.dto.request.OrderCompleteRequest;
import project.shopclone.domain.order.dto.request.OrderPrepareRequest;
import project.shopclone.domain.order.service.PaymentService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    // 결제금액 사전등록
    @PostMapping("/prepare")
    public ResponseEntity<String> prepareOrder(@RequestHeader("Authorization") String token,
                                                             @RequestBody OrderPrepareRequest prepareRequest) throws IamportResponseException, IOException {
        String merchantId = paymentService.prepare(token, prepareRequest);
        return ResponseEntity.ok(merchantId);
    }

    // 결제검증 후 저장 or 취소
    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestHeader("Authorization") String token,
                                                              @RequestBody OrderCompleteRequest orderCompleteRequest) throws IamportResponseException, IOException {
        return paymentService.completeOrder(token, orderCompleteRequest);
    }

    // 환불하기
    @PostMapping("/cancel/{merchantUid}")
    public ResponseEntity<String> cancelOrder(@RequestHeader("Authorization") String token,
                                              @PathVariable String merchantUid) throws IamportResponseException, IOException {
        return paymentService.cancelOrder(token, merchantUid);
    }

}
