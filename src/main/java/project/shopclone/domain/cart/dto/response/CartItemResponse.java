package project.shopclone.domain.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.cart.entity.CartItem;
import project.shopclone.domain.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;

    private String image;    // 이미지
    private String name;     // 상품정보
    private Integer price;   // 판매가
    private Integer count;   // 수량

    public static CartItemResponse from(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.cartItemId = cartItem.getCartItemId();
        cartItemResponse.count = cartItem.getCount();

        Product product = cartItem.getProduct();
        cartItemResponse.productId = product.getId();
        cartItemResponse.image = product.getImage();
        cartItemResponse.name = product.getName();
        cartItemResponse.price = product.getPrice();
        return cartItemResponse;
    }
}
