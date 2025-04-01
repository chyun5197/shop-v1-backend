package project.shopclone.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.domain.user.dto.response.AuthUserLoginResponse;
import project.shopclone.global.jwt.refreshtoken.RefreshToken;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenRepository;
import project.shopclone.global.jwt.service.TokenService;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthUserRepository authUserRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        System.out.println("request.getRequestURL() = " + request.getRequestURL());
//        System.out.println("authentication.getName() = " + authentication.getName());

        AuthUser authuser = authUserRepository.findByEmail(authentication.getName())
                .orElseThrow();

        // 로그인할때 리프레시 토큰과 액세스 토큰 생성
        String refreshToken = tokenService.createNewRefreshToken(authuser);
        String accessToken = tokenService.createNewAccessToken(authuser, refreshToken);

        // Redis에 리프레시 토큰 저장
        RefreshToken redis = new RefreshToken(refreshToken, authuser.getId());
        refreshTokenRepository.save(redis);

        AuthUserLoginResponse authUserLoginResponse = new AuthUserLoginResponse(authuser.getEmail(), refreshToken, accessToken);
        String valueAsString = objectMapper.writeValueAsString(authUserLoginResponse);
//        response.addCookie();
        response.getWriter().write(valueAsString);
    }
}
