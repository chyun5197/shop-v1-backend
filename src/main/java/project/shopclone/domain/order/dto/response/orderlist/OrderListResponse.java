package project.shopclone.domain.order.dto.response.orderlist;

import lombok.Getter;
import lombok.Setter;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;

import java.util.List;

@Getter
@Setter
public class OrderListResponse { // 주문 조회
    private Long orderId;               // 주문ID
    private String merchantUid;         // 주문번호
    private String orderDate;           // 주문일자
    private Integer price;              // 실제상품가격
    private String payCard;             // 결제카드
    private String payMethod;           // 결제방법
    private String paymentStatus;       // 결제상태

    private List<OrderItemListResponse> orderItems; // 주문 아이템 리스트

    public static OrderListResponse of(Orders order, Payment payment) {
        OrderListResponse orderListResponse = new OrderListResponse();
        orderListResponse.setOrderId(order.getOrderId());
        orderListResponse.setMerchantUid(order.getMerchantUid());
        orderListResponse.setOrderDate(String.valueOf(order.getOrderDate()).substring(0, 10));
        orderListResponse.setPrice(payment.getRealPrice());
        orderListResponse.setPayMethod(payment.getPayMethod());
        orderListResponse.setPayCard(payment.getPayCard());

        switch (payment.getPaymentStatus()){
            case READY:
                orderListResponse.setPaymentStatus("결제대기");
                break;
            case FAILED:
                orderListResponse.setPaymentStatus("결제취소");
                break;
            case PAID:
                orderListResponse.setPaymentStatus("결제완료");
                break;
            case CANCEL:
                orderListResponse.setPaymentStatus("환불완료");
                break;
            default:
                orderListResponse.setPaymentStatus("확인불가");
        }

        orderListResponse.setOrderItems(order.getOrderItemList().stream()
                .map(OrderItemListResponse::from)
                .toList()
        );
        return orderListResponse;
    }
}
