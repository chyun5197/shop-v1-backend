package project.shopclone.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import project.shopclone.domain.product.entity.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(
        name="order_item",
        uniqueConstraints= { // 복합키 설정 (중복 저장 불가)
                @UniqueConstraint(
                        columnNames = {"order_id", "product_id"}
                )
        }
)
public class OrderItem { // 주문 아이템
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Orders orders;

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

    private Integer quantity;   // 수량
}
