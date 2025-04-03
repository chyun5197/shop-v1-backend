package project.shopclone.domain.order.dto.response.orderlist;

import lombok.Getter;
import lombok.Setter;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.product.entity.Product;

@Getter
@Setter
public class OrderItemResponse { // 주문 아이템 조회
    private Long orderItemId;   // 주문 아이템ID
    private Long productId;     // 상품ID
    private String image;    // 이미지
    private String productName; // 상품명
    private int quantity;       // 수량

    public static OrderItemResponse from(OrderItem orderItem) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setOrderItemId(orderItem.getOrderItemId());
        orderItemResponse.setQuantity(orderItem.getQuantity());

        Product product = orderItem.getProduct();
        orderItemResponse.setProductId(product.getId());
        orderItemResponse.setProductName(product.getName());
        orderItemResponse.setImage(product.getImage());
        return orderItemResponse;
    }
}
