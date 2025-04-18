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

    // 분산락 구현중에 추가로 발생한 동시성 문제는 이녀석의 즉시로딩이 원인이었다 -> 지연로딩으로 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private Integer quantity;   // 수량
}
