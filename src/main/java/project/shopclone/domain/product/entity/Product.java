package project.shopclone.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // 상세 이미지
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> prdouctImageList = new ArrayList<>();

    @Builder
    public Product(Integer no, String category, String brand, String series, String name, Integer price, Integer originPrice, Integer discountRate, String image, String description, String model, String country, Integer releaseDate) {
        this.no = no;
        this.category = category;
        this.brand = brand;
        this.series = series;
        this.name = name;
        this.price = price;
        this.originPrice = originPrice;
        this.discountRate = discountRate;
        this.image = image;
        this.description = description;
        this.model = model;
        this.country = country;
        this.releaseDate = releaseDate;
    }

}
