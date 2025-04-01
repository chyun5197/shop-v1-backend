package project.shopclone.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderItemSummary {
    private Long productId;     // 상품번호
    private Integer quantity;   // 수량

    private String image;       // 이미지
    private String name;        // 상품정보
    private Integer price;      // 판매가

    public static OrderItemSummary of(Product product, Integer quantity) {
        OrderItemSummary orderItemSummary = new OrderItemSummary();
        orderItemSummary.productId = product.getId();
        orderItemSummary.quantity = quantity;
        orderItemSummary.image = product.getImage();
        orderItemSummary.name = product.getName();
        orderItemSummary.price = product.getPrice();
        return orderItemSummary;
    }
}
