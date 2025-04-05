package project.shopclone.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
}
