package project.shopclone.global.jwt.refreshtoken;//package project.shopclone.global.jwt.refreshtoken;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 28800) // 8시간. 계산기 - https://ttl-calc.com
public class RefreshToken {
    @Id // 리프레시 토큰으로 아이디 찾기
    private String refreshToken;

    private Long authuserId;

    public RefreshToken(String refreshToken, Long authuserId ) {
        this.refreshToken = refreshToken;
        this.authuserId = authuserId;
    }
}
