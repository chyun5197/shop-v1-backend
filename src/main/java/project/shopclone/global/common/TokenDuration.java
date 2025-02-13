package project.shopclone.global.common;

import java.time.Duration;

public final class TokenDuration {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofHours(4); // 리프레시 토큰 기간 = Redis에 넣을 리프레시 토큰 TTL
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(4); // 액세스 토큰 기간
}
