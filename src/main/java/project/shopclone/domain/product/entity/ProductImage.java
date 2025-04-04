package project.shopclone.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name="product_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // 내부 조인
    private Product product;

    private String imageDetail;

    @Builder
    public ProductImage(Product product, String imageDetail) {
        this.product = product;
        this.imageDetail = imageDetail;
    }
}
