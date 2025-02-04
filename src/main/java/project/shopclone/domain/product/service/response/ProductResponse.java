package project.shopclone.domain.product.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductResponse {
    private Long id;

    // 상위에서 할당
    private Integer no;             // 원본사이트에서의 상품번호
    private String category;        // 대분류 g1, g2, b1..
    private String brand;           // 중분류 fender, gibson, prs..
    private String series;          // 소분류 (현재 할당X)

    // heading area
    private String name;            // 제목: 모델명 + 옵션 + (new/old) + 시리얼넘버  [세일 멘트 제외함]
    private Integer price;          // 판매가: 예약중인 상품은 제외
    private Integer originPrice;    // 원가: 없는 경우 null
    private Integer discountRate;   // 할인율: 없는 경우 null
    private String image;           // 현재는 원본 사이트의 url

    // 본문 (현재 할당X)
    private String description;     // 본문 설명

    // table
    private String model;           // 모델명
    private String country;         // 원산지
    private Integer releaseDate;    // 출시 연도

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.no = product.getNo();
        this.category = product.getCategory();
        this.brand = product.getBrand();
        this.series = product.getSeries();
        this.name = product.getName();
        this.price = product.getPrice();
        this.originPrice = product.getOriginPrice();
        this.discountRate = product.getDiscountRate();
        this.image = product.getImage();
        this.description = product.getDescription();
        this.model = product.getModel();
        this.country = product.getCountry();
        this.releaseDate = product.getReleaseDate();
    }
}
