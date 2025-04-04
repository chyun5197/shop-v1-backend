package project.shopclone.domain.order.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.order.dto.request.OrderCompleteRequest;
import project.shopclone.domain.order.dto.request.OrderPrepareRequest;
import project.shopclone.domain.order.service.PaymentService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "결제 API")
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제금액 사전등록")
    @PostMapping("/prepare")
    public ResponseEntity<String> prepareOrder(@RequestHeader("Authorization") String token,
                                                             @RequestBody OrderPrepareRequest prepareRequest) throws IamportResponseException, IOException {
        String merchantId = paymentService.prepare(token, prepareRequest);
        return ResponseEntity.ok(merchantId);
    }

    @Operation(summary = "결제 검증 후 결제 승인/취소 처리")
    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestHeader("Authorization") String token,
                                                              @RequestBody OrderCompleteRequest orderCompleteRequest) throws IamportResponseException, IOException {
        return paymentService.completeOrder(token, orderCompleteRequest);
    }

    @Operation(summary = "환불하기")
    @PostMapping("/cancel/{merchantUid}")
    public ResponseEntity<String> cancelOrder(@RequestHeader("Authorization") String token,
                                              @PathVariable String merchantUid) throws IOException, IamportResponseException {
        return paymentService.cancelOrder(token, merchantUid);
    }

}
