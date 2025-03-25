package project.shopclone.global.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberRepository;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.exception.AuthUserErrorCode;
import project.shopclone.domain.user.exception.AuthUserException;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.global.jwt.refreshtoken.RefreshToken;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenRepository;
import project.shopclone.global.jwt.service.TokenProvider;

import java.io.IOException;
import java.util.Map;

import static project.shopclone.global.common.TokenDuration.ACCESS_TOKEN_DURATION;
import static project.shopclone.global.common.TokenDuration.REFRESH_TOKEN_DURATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.oauth-redirect}")
    public String redirect_path; // 로그인 성공 후 리다이렉트할 프론트 페이지

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final AuthUserRepository authUserRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 응답 파라미터 확인용
//        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
//        System.out.println("oAuth2User.getAuthorities() = " + oAuth2User.getAuthorities());

        String oauthId;
        if(attributes.get("email") != null) { // 구글
            oauthId = (String) attributes.get("sub");
        }else if(attributes.get("connected_at")!=null){ // 카카오 (네이버에 없는 파라미터(connected_at)로 구분)
            oauthId = attributes.get("id").toString();
        }else{ // 네이버
            Map attributesResponse = (Map) attributes.get("response");
            oauthId = attributesResponse.get("id").toString();
        }

        AuthUser authUser = authUserRepository.findByOauthId(oauthId)
                    .orElseThrow(() -> new AuthUserException(AuthUserErrorCode.USER_NOT_FOUND));


        // 계정으로 연결된 멤버 없으면 생성
        if (memberRepository.findByAuthUser(authUser) == null) {
            memberRepository.save(Member.builder()
                    .email(authUser.getEmail())
                    .name(authUser.getNickname())
                    .authUser(authUser)
                    .build());
        }

        // 리프레시 토큰 생성 -> DB에 저장 -> 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(authUser, REFRESH_TOKEN_DURATION);
        saveRefreshToken(authUser.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken); // 쿠키에 토큰 저장 제외

        // 액세스 토큰 생성 -> 패스에 엑세스 토큰 추가
        String accessToken = tokenProvider.generateToken(authUser, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken, refreshToken); // 액세스, 리프레시 모두 전달
//        String targetUrl = getTargetUrl(accessToken);
//        log.info("OA2S accessToken: {}", accessToken);
//        log.info("OA2S refreshToken: {}", refreshToken);

        // 인증 관련 설정값과 쿠키 제거
        clearAuthenticationAttributes(request, response);

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 생성된 리프레시 토큰을 전달받아 유저 아이디와 데이터베이스에 저장
    private void saveRefreshToken(Long authUserId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAuthUserId(authUserId)
                .map(entity -> entity.update(newRefreshToken)) // 회원ID 대응되는 리프레시토큰 엔티티가 기존에 있으면 업데이트
                .orElse(new RefreshToken(newRefreshToken, authUserId)); // 없으면 새로 생성
        refreshTokenRepository.save(refreshToken);
    }

    // 액세스 토큰을 패스에 추가
    // 쿠키에서 리다이렉트 경로가 담긴 값을 가져와 쿼리 파라미터에 액세스 토큰을 추가한다
    // 액세스 토큰을 클라이언트에게 전달
    private String getTargetUrl(String access, String refresh) {
        return UriComponentsBuilder.fromUriString(redirect_path)
                .queryParam("access_token", access)
//                .queryParam("refresh", refresh) // 쿠키로 보냄
                .build()
                .toUriString();
    }

    // 인증 관련 설정값과 쿠키 제거
    // 인증 프로세스를 진행하면서 세션과 쿠키에 임시로 저장해둔 인증 관련 데이터를 제거한다
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // (미사용)
    // 생성된 리프레시 토큰을 쿠키에 저장.
    // 클라이언트에서 액세스 토큰이 만료되면 재발급 요청하도록 해당 메서드로 쿠키에 리프레시 토큰을 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, "refresh_token"); // 백엔드 로컬로 실험하는 용도
        CookieUtil.addCookie(response, "refresh_token", refreshToken, cookieMaxAge);
    }
}
