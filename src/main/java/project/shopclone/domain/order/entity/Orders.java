package project.shopclone.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.shopclone.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name="orders")
public class Orders { // 주문정보
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList;

    @CreatedDate
    private LocalDateTime orderDate;

    private String merchantUid;     // 쇼핑몰 주문번호
    private String address;         // 주소

    private String orderName;       // 주문자명
    private String orderEmail;      // 이메일

    // 변화
    private Boolean paymentStatus;  // 결제 상태

    private Integer totalQuantity;  // 수량
    private Integer totalPrice;     // 총가격
}
