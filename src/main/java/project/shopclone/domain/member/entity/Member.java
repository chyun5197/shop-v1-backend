package project.shopclone.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import project.shopclone.domain.member.dto.MemberUpdateRequest;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.wish.Wish;
import project.shopclone.global.common.BaseTime;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="member")
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "email")
    private String email;

    private String name;
    private String address;
    private String phone;

    private Integer wishCount;
    private Integer cartCount;
    private Integer savings; // 적립금

    @OneToOne
    @JoinColumn(name="authuser_id")
    private AuthUser authUser;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> ordersList;

    @Builder
    public Member(String email, String name, String address, String phone, AuthUser authUser) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.authUser = authUser;
        this.wishCount = 0;
        this.cartCount = 0;
        this.savings = 1000;
    }

    public void plusWishCount(){
        this.wishCount++;
    }
    public void minusWishCount(){
        if(this.wishCount > 0){
            this.wishCount--;
        }
    }
    public void updateWishCount(int num){
        this.wishCount += num;
        this.wishCount = this.wishCount > 0 ? this.wishCount : 0;
    }


    public void plusCartCount(){
        this.cartCount += 1;
    }
    public void minusCartCount(){
        if(this.cartCount > 0){
            this.cartCount -= 1;
        }
    }
    public void updateCartCount(int num){
        this.cartCount += num;
        this.cartCount = this.cartCount > 0 ? this.cartCount : 0;
    }

    public void updateMember(MemberUpdateRequest memberUpdateRequest) {
        if(memberUpdateRequest.getName()!=null){
            this.name = memberUpdateRequest.getName();
        }
        if(memberUpdateRequest.getAddress()!=null){
            this.address = memberUpdateRequest.getAddress();
        }
        if(memberUpdateRequest.getPhone()!=null){
            this.phone = memberUpdateRequest.getPhone();
        }
    }
}
