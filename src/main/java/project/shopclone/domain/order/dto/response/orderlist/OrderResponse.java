package project.shopclone.domain.order.dto.response.orderlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse { // 주문 조회
    private Long orderId;               // 주문ID
    private String merchantUid;         // 주문번호
    private LocalDateTime orderDate;    // 주문일자
    private Integer price;              // 결제가격
    private String payMethod;           // 결제방법
    private Boolean paymentStatus;      // 결제상태

    private List<OrderItemResponse> orderItems; // 주문 아이템 리스트

    public static OrderResponse of(Orders order, Payment payment) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setMerchantUid(order.getMerchantUid());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setPrice(1000);
        orderResponse.setPayMethod(payment.getPayMethod());
        orderResponse.setPaymentStatus(payment.getPaymentStatus());

        orderResponse.setOrderItems(order.getOrderItemList().stream()
                .map(OrderItemResponse::from)
                .toList()
        );
        return orderResponse;
    }
}
