package project.shopclone.domain.cart.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.cart.entity.CartItem;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartResponse {
    private Integer cartCount;
    private Integer savings;
    private List<CartItemResponse> cartItemList;
    // 금액 계산은 리액트에서
}
