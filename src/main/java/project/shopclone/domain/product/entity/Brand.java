package project.shopclone.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String category;
    private String image;

    private Integer cateNo;  // 원본사이트 cate_no = 브랜드 넘버


    @Builder
    public Brand(String brand, String category, String image, Integer cateNo) {
        this.brand = brand;
        this.category = category;
        this.image = image;
        this.cateNo = cateNo;
    }
}
