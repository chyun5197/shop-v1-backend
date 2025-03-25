package project.shopclone.global.jwt.refreshtoken;//package project.shopclone.global.jwt.refreshtoken;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 3600 * 8) // 8시간
public class RefreshToken {
    @Id // 리프레시 토큰으로 아이디 찾기
    private String refreshToken;

    private Long authUserId;

    public RefreshToken(String refreshToken, Long authUserId ) {
        this.refreshToken = refreshToken;
        this.authUserId = authUserId;
    }

    public RefreshToken update(String newRefreshToken){
        this.refreshToken = newRefreshToken;
        return this;
    }
}
