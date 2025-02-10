package project.shopclone.global.jwt.refreshtoken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.shopclone.global.jwt.TokenProvider;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    // 전달 받은 리프레시 토큰으로 리프레시 토큰 객체를 검색해서 전달하는 메서드
    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰을 찾을 수 없습니다."));
    }

    @Transactional
    public void delete() {
        // 회원 검색은 액세스토큰으로(현재의 시컨홀에 들어있는걸 가져옴).
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getAuthUserId(token);

        // 삭제는 리프레시 토큰을.
        refreshTokenRepository.deleteByMemberId(userId);
    }
}