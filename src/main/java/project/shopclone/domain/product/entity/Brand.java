package project.shopclone.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;
    private String brand;
    private String category;
    private String image;

    private Integer cateNo;  // 원본사이트 cate_no = 브랜드 넘버
}
