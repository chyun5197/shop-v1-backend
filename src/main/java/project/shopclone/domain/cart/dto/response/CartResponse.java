package project.shopclone.domain.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
