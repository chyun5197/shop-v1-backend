package project.shopclone.domain.order.dto.response.orderlist;

import lombok.Getter;
import lombok.Setter;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.product.entity.Product;

@Getter
@Setter
public class OrderItemListResponse { // 주문 아이템 조회
    private Long orderItemId;   // 주문 아이템ID
    private Long productId;     // 상품ID
    private String image;       // 이미지
    private String productName; // 상품명
    private Integer realPrice;  // 실제 가격
    private int quantity;       // 수량

    public static OrderItemListResponse from(OrderItem orderItem) {
        OrderItemListResponse orderItemListResponse = new OrderItemListResponse();
        orderItemListResponse.setOrderItemId(orderItem.getOrderItemId());
        orderItemListResponse.setQuantity(orderItem.getQuantity());

        Product product = orderItem.getProduct();
        orderItemListResponse.setRealPrice(product.getPrice());
        orderItemListResponse.setProductId(product.getId());
        orderItemListResponse.setProductName(product.getName());
        orderItemListResponse.setImage(product.getImage());
        return orderItemListResponse;
    }
}
