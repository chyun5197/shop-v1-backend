package project.shopclone.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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
    private Integer paidPrice;      // 결제 가격 (테스트 결제 1000원 고정)
    private Integer totalQuantity;  // 결제 수량

    // 포트원으로부터 응답
    private Boolean paymentStatus;  // 결제 상태
    private String payMethod;       // 결제 방법 (카드사; 포트원의 emb_pg_provider)
    private String buyerName;       // 구매자 이름
    private String buyerEmail;      // 구매자 이메일
//    private String buyerTel;
//    private String buyerAddress;

    @CreatedDate
    private LocalDateTime paidAt;   // 결제 일자

    public void paymentSuccess(com.siot.IamportRestClient.response.Payment portOneResponse, String impUid) {
        this.paymentStatus = true;
        this.payMethod = portOneResponse.getEmbPgProvider();
        this.buyerName = portOneResponse.getBuyerName();
        this.buyerEmail = portOneResponse.getBuyerEmail();
        // Date -> LocalDateTime 변환
        this.paidAt = portOneResponse.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.impUid = impUid;
    }

}
