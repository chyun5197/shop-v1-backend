package project.shopclone.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.shopclone.domain.user.AuthUser;
import project.shopclone.domain.user.AuthUserService;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenService;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthUserService authUserService;

    // 전달 받은 리프레시 토큰으로 토큰 유효성 검사 진행하고,
    // 유효한 토큰일 떄 리프레시 토큰으로 사용자 ID를 찾는 메서드
    public String createNewAccessToken(String refreshToken){
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)){
//            log.info("토큰 유효 검증 실패");
            throw new IllegalArgumentException("유효한 리프레시 토큰이 아닙니다.");
        }
        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getAuthuserId();
        AuthUser authUser = authUserService.findById(memberId);
//        log.info("토큰 유효 검증 성공");

        // 사용자 ID로 사용자를 찾은 후에 토큰 제공자의 generateToken() 메서드를 호출해 새로운 액세스 토큰을 생성
        return tokenProvider.generateToken(authUser, Duration.ofHours(2));
    }

}
