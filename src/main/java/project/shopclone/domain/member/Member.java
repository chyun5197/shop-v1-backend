package project.shopclone.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.user.AuthUser;

@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String name;
    private String address;
    private String phone;

    private Integer wishCount;
    private Integer cartCount;

    @OneToOne
    @JoinColumn(name="authuser_id")
    private AuthUser authUser;

    @Builder
    public Member(String email, String name, String address, String phone, Integer wishCount, Integer cartCount, AuthUser authUser) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.wishCount = wishCount;
        this.cartCount = cartCount;
        this.authUser = authUser;
    }
}
