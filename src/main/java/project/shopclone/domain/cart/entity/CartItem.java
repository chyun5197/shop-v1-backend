package project.shopclone.domain.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.shopclone.domain.product.entity.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(
        name="cart_item",
        uniqueConstraints= { // 복합키 설정 (중복 저장 불가)
                @UniqueConstraint(
                        columnNames = {"cart_id", "product_id"}
                )
        }
)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    private Integer count;   // 수량

    // 상품 정보
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public void updateCount(Integer count){
        this.count = count;
    }

    @CreatedDate
    private LocalDateTime createdAt;
}
