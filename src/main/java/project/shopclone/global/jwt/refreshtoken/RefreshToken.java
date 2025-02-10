package project.shopclone.global.jwt.refreshtoken;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
    // 리프레시 토큰은 데이터베이스에 저장하는 정보이므로 엔티티와 리포지터리를 추가해야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "authuser_id", nullable = false, unique = true)
    private Long authuserId;

    @Column(name = "member_id", unique = true)
    private Long memberId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(Long authuserId, Long memberId, String refreshToken) {
        this.authuserId = authuserId;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public RefreshToken update(String newRefreshToken){
        this.refreshToken = newRefreshToken;
        return this;
    }
}
