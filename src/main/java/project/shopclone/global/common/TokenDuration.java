package project.shopclone.global.common;

import java.time.Duration;

public final class TokenDuration {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(1); // 리프레시 토큰 기간
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 액세스 토큰 기간
}
