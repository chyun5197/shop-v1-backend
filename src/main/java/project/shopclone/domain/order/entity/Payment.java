package project.shopclone.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Orders orders;

    private String merchantUid;     // 쇼핑몰 주문번호
    private String impUid;          // 포트원 고유 주문번호
    private Integer realPrice;      // 실제 가격
    private Integer paidPrice;      // 결제 가격
    private Integer totalQuantity;  // 결제 수량
    private String payMethod;       // 결제 방법
    private Boolean paymentStatus;  // 결제 상태

    @CreatedDate
    private LocalDateTime paidAt;   // 결제 일자

}
