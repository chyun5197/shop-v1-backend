package project.shopclone.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.order.dto.request.OrderItemRequest;
import project.shopclone.domain.order.dto.response.OrderSheetResponse;
import project.shopclone.domain.order.dto.response.orderlist.OrderListResponse;
import project.shopclone.domain.order.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 API")
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문서 만들기")
    @PostMapping("/sheet")
    public ResponseEntity<OrderSheetResponse> createOrderSheet(@RequestHeader("Authorization") String token,
                                                               @RequestBody List<OrderItemRequest> orderItemRequestList
                                              ){
        return orderService.createOrderSheet(token, orderItemRequestList);
    }

    @Operation(summary = "주문내역 조회")
    @GetMapping("/list")
    public ResponseEntity<List<OrderListResponse>> getOrderList(@RequestHeader("Authorization") String token){
        return orderService.getOrderList(token);
    }

}
