package project.shopclone.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductThumbResponse {
    private Long id;

    private Integer no;             // 원본사이트에서의 상품번호
    private String category;        // 대분류 g1, g2, b1..
    private String brand;           // 중분류 fender, gibson, prs..
//    private String series;          // 소분류 (현재 할당X)

    private String name;            // 제목: 모델명 + 옵션 + (new/old) + 시리얼넘버  [세일 멘트 제외함]
    private Integer price;          // 판매가: 예약중인 상품은 제외
    private Integer originPrice;    // 원가: 없는 경우 null
    private String image;           // 현재는 원본 사이트의 url
    private String cdnImage;        // CloudFront url
    private Integer wishCount;      // 위시 개수

    public static ProductThumbResponse from(Product product) {
        ProductThumbResponse response = new ProductThumbResponse();
        response.id = product.getId();
        response.no = product.getNo();
        response.category = product.getCategory();
        response.brand = product.getBrand();
        response.name = product.getName();
        response.price = product.getPrice();
        response.originPrice = product.getOriginPrice();
        response.image = product.getImage();
        response.cdnImage = product.getCdnImage();
        response.wishCount = product.getWishCount();
        return response;
    }
}
