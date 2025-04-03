package project.shopclone.domain.order.controller;

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
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/sheet")
    public ResponseEntity<OrderSheetResponse> createOrderSheet(@RequestHeader("Authorization") String token,
                                                               @RequestBody List<OrderItemRequest> orderItemRequestList
                                              ){
        return  orderService.createOrderSheet(token, orderItemRequestList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderListResponse>> getOrderList(@RequestHeader("Authorization") String token){
        return orderService.getOrderList(token);
    }

}
