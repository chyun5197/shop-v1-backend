package project.shopclone.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenService;

import static project.shopclone.global.common.TokenDuration.ACCESS_TOKEN_DURATION;
import static project.shopclone.global.common.TokenDuration.REFRESH_TOKEN_DURATION;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    // 리프레시 토큰으로 Redis에서 인증회원ID 반환하면서 캐싱
//    @Cacheable(cacheNames = "token", key="'refreshToken:' + #refreshToken")
//    public Long getAuthUserIdRefreshToken(String refreshToken) {
//        return refreshTokenService.findByRefreshToken(refreshToken).getAuthuserId();
//    }

    // 리프레시 토큰 발급
    public String createNewRefreshToken(AuthUser authUser) {
        return tokenProvider.generateToken(authUser, REFRESH_TOKEN_DURATION);
    }

    // 액세스 토큰 발급(by 리프레시 토큰)
    public String createNewAccessToken(AuthUser authUser, String refreshToken){
        // 리프레시 토큰 유효성 검사
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("유효한 리프레시 토큰이 아닙니다."); // 토큰 유효성 검사에 실패하면 예외 발생
        }
//        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getAuthuserId();
//        AuthUser authUser = authUserService.findById(memberId);

        // 사용자 ID로 사용자를 찾은 후에 토큰 제공자의 generateToken() 메서드를 호출해 새로운 액세스 토큰을 생성
        return tokenProvider.generateToken(authUser, ACCESS_TOKEN_DURATION);
    }
}
