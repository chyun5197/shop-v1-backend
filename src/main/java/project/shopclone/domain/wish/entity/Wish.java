package project.shopclone.domain.wish.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.product.entity.Product;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(
        name="wish",
        uniqueConstraints= { // 복합키 설정 (중복 저장 불가)
                @UniqueConstraint(
                        columnNames = {"member_id", "product_id"}
                )
        }
)
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @CreatedDate
    private LocalDateTime createdAt;
}
