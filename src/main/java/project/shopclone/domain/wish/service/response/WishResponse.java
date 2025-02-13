package project.shopclone.domain.wish.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.wish.Wish;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WishResponse {
    private Long wishId;
    private Long productId;

    private String image;    // 이미지
    private String name;     // 상품정보
    private Integer price;   // 판매가

    public static WishResponse from(Wish wish) {
        WishResponse wishResponse = new WishResponse();
        wishResponse.wishId = wish.getWishId();

        Product product = wish.getProduct();
        wishResponse.productId = product.getId();
        wishResponse.image = product.getImage();
        wishResponse.name = product.getName();
        wishResponse.price = product.getPrice();
        return wishResponse;
    }
}
