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
    private String orderDate;    // 주문일자
    private Integer price;              // 실제상품가격
    private String payCard;             // 결제 카드
    private String payMethod;           // 결제방법
    private String paymentStatus;      // 결제상태

    private List<OrderItemResponse> orderItems; // 주문 아이템 리스트

    public static OrderResponse of(Orders order, Payment payment) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setMerchantUid(order.getMerchantUid());
        orderResponse.setOrderDate(String.valueOf(order.getOrderDate()).substring(0, 10));
        orderResponse.setPrice(payment.getRealPrice());
        orderResponse.setPayMethod(payment.getPayMethod());
        orderResponse.setPayCard(payment.getPayCard());

        switch (payment.getPaymentStatus()){
            case READY:
                orderResponse.setPaymentStatus("결제대기");
                break;
            case FAILED:
                orderResponse.setPaymentStatus("결제취소");
                break;
            case PAID:
                orderResponse.setPaymentStatus("결제완료");
                break;
            case CANCEL:
                orderResponse.setPaymentStatus("환불완료");
                break;
            default:
                orderResponse.setPaymentStatus("확인불가");
        }

        orderResponse.setOrderItems(order.getOrderItemList().stream()
                .map(OrderItemResponse::from)
                .toList()
        );
        return orderResponse;
    }
}
