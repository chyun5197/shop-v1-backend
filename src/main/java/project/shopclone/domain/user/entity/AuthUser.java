package project.shopclone.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.shopclone.global.common.BaseTime;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
//UserDetails를 상속 받아 인증 객체로 사용
//사용자의 인증 정보와 권한 정보를 저장하는 메서드 제공
public class AuthUser extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "oauth_id", nullable = false, unique = true)
    private String oauthId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    // 소셜로그인 시 계정에서 가져올 닉네임
    @Column(name="nickname")
    private String nickname;

    // 소셜로그인 채널
    private String channel;

//    @OneToOne(mappedBy = "auth_user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Member member;

    @Builder
    public AuthUser(String email, String password, String nickname, String channel, String oauthId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.channel = channel;
        this.oauthId = oauthId;
    }

    /* ================= implements from UserDetails ================= */
    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자 id를 반환(고유한 값)
    public String getUsername() {
        return email;
    }

    @Override // 사용자 패스워드 반환
    public String getPassword() {
        return password;
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않음
    }

    @Override //패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }



}
