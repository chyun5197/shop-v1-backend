package project.shopclone.global.common;

import java.time.Duration;

public final class TokenDuration {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofHours(8); // 리프레시 토큰 기간 = Redis에 넣을 리프레시 토큰 TTL
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(4); // 액세스 토큰 기간. 리액트에서의 토큰 재발급 기능 미완성으로 길게 설정
}
