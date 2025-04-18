package project.shopclone.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    private String category;        // 대분류 Guitars 1, 2..
    private String brand;           // 중분류 Fender, Gibson..
    private String series;          // 소분류 (현재 할당X)

    private Integer cateNo;          // 카테고리 넘버

    // heading area
    private String name;            // 제목: 모델명 + 옵션 + (new/old) + 시리얼넘버  [세일 멘트 제외함]
    private Integer price;          // 판매가: 예약중인 상품은 제외
    private Integer originPrice;    // 원가: 없는 경우 null
    private Integer discountRate;   // 할인율: 없는 경우 null
    private String image;           // 원본 사이트의 url
    private String cdnImage;        // (사용)도메인 cdn url

    // 본문 (현재 할당X)
    private String description;     // 본문 설명

    // 하단표 정보
    private String model;           // 모델명
    private String country;         // 원산지
    private Integer releaseDate;    // 출시 연도
    private String inst;            // 악기 3개 카테고리 (Guitar, Bass, Acoustic)

    private Integer stock;          // 재고 수량

    @Version
    private Integer wishCount;      // 위시 개수(좋아요 수)

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
        this.wishCount = 0;
    }

    public void updateCateNo(Integer cateNo) {
        this.cateNo = cateNo;
    }

    public void updateCdnUrl(String cdnImage) {
        this.cdnImage = cdnImage;
    }

    public void plusWishCount() {
        this.wishCount++;
    }

    public void minusWishCount() {
        if (this.wishCount > 0) {
            this.wishCount--;
        }
    }

    public void updateStock(int stock) {
        this.stock = stock;
    }

    public boolean removeStock(Integer quantity) {
        if (this.stock - quantity < 0) {
            log.info("실패");
            return false;
        }
        log.info("성공");
        this.stock -= quantity;
        return true;
    }

}
