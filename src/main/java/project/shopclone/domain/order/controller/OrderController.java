package project.shopclone.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.order.dto.request.OrderItemRequest;
import project.shopclone.domain.order.dto.response.OrderSheetResponse;
import project.shopclone.domain.order.entity.OrderItem;
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
        System.out.println("실행");
        System.out.println("orderItemRequestList = " + orderItemRequestList);
        for(OrderItemRequest orderItemRequest : orderItemRequestList){
            System.out.println("orderItem = " + orderItemRequest.getProductId());
            System.out.println("orderItem = " + orderItemRequest.getQuantity());
        }
        return  orderService.createOrderSheet(token, orderItemRequestList);
    }

}
