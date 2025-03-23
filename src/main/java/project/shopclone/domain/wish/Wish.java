package project.shopclone.domain.wish;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.product.entity.Product;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name="wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;
    // sql에서 create unique index 만들었음

    @CreatedDate
    private LocalDateTime createdAt;
}
