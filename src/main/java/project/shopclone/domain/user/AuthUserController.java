package project.shopclone.domain.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.user.service.request.AuthUserAddRequest;
import project.shopclone.domain.user.service.response.AuthUserLoginResponse;
import project.shopclone.domain.user.service.AuthUserService;
import project.shopclone.global.jwt.refreshtoken.RefreshToken;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenService;
import project.shopclone.global.jwt.service.TokenProvider;
import project.shopclone.global.jwt.service.TokenService;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenRepository;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class AuthUserController {
    private final AuthUserService authUserService;
    private final AuthUserRepository authUserRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

//    @PostMapping("/login") => WebSecurityConfig

//    @GetMapping("/test")
//    public void test(){
//        securityUserService.test();
//    }


    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody AuthUserAddRequest authUserAddRequest) {
        log.info("email : {}", authUserAddRequest.getEmail());
        log.info("password : {}", authUserAddRequest.getPassword());
        authUserService.save(authUserAddRequest);
        return "signup success";
    }

    // 로그인 성공시 동작할 메서드. 브라우저에 리프레시, 액세스 토큰 모두 전달
    @GetMapping("/login")
    public AuthUserLoginResponse loginSuccessAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

//        System.out.println("로그인한 유저 이메일: " + email);
//        System.out.println("유저 권한: " + authentication.getAuthorities());
//        System.out.println("authentication.getCredentials() = " + authentication.getCredentials());

        AuthUser authuser = authUserRepository.findByEmail(email);

        // 로그인할때 리프레시 토큰과 액세스 토큰 생성
        String refreshToken = tokenService.createNewRefreshToken(authuser);
        String accessToken = tokenService.createNewAccessToken(authuser, refreshToken);

        // Redis에 리프레시 토큰 저장
        RefreshToken redis = new RefreshToken(refreshToken, authuser.getId());
        refreshTokenRepository.save(redis);

        // 리프레시 토큰으로 ID 찾는 메서드를 Redis에 캐싱
        tokenService.getAuthUserIdRefreshToken(refreshToken);

        // 이메일, 리프레시 토큰, 액세스 토큰 전달
        return new AuthUserLoginResponse(email, refreshToken, accessToken);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
//                                         ,@RequestHeader("Authorization") String refreshToken) {
        // Redis에서 토큰도 삭제?
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().body("logout success");
    }

}
