package project.shopclone.domain.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import project.shopclone.global.jwt.TokenService;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class AuthUserController {
    private final AuthUserService authUserService;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthUserRepository authUserRepository;

//    @PostMapping("/login") => WebSecurityConfig

//    @GetMapping("/test")
//    public void test(){
//        securityUserService.test();
//    }


    @PostMapping("/signup")
    public String signup(@RequestBody AuthUserAddRequest authUserAddRequest) {
        log.info("email : {}", authUserAddRequest.getEmail());
        log.info("password : {}", authUserAddRequest.getPassword());
        authUserService.save(authUserAddRequest);
        return "signup success";
    }

    @GetMapping("/login")
    public ResponseEntity<AuthUserLoginResponse> loginSuccess() {
        log.info("controller /login 진입");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String authorities = authentication.getAuthorities().toString();

        System.out.println("로그인한 유저 이메일: " + email);
        System.out.println("유저 권한: " + authentication.getAuthorities());
        System.out.println("authentication.getCredentials() = " + authentication.getCredentials());

        AuthUser authuser = authUserRepository.findByEmail(email);
        String refreshToken = refreshTokenRepository.findByAuthuserId(authuser.getId()).getRefreshToken();
        String accessToken = tokenService.createNewAccessToken(refreshToken);

//        Map<String, String> userInfo = new HashMap<>();
//        userInfo.put("email", email);
//        userInfo.put("authorities", authorities);
//        userInfo.put("id", authorities);

        return ResponseEntity.ok(new AuthUserLoginResponse(email, accessToken));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("로그아웃 성공");
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().body("logout success");
    }

}
