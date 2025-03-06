package project.shopclone.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.service.request.AuthUserAddRequest;
import project.shopclone.domain.user.service.response.AuthUserLoginResponse;
import project.shopclone.domain.user.service.AuthUserService;
import project.shopclone.global.jwt.refreshtoken.RefreshToken;
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

    // 아이디 중복 여부
    @GetMapping("/check/{email}")
    public ResponseEntity<Integer> checkAuthUser(@PathVariable String email) {
        return ResponseEntity.ok().body(authUserRepository.findByEmail(email) != null ? 1 : 0); // 중복 : 사용가능
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody AuthUserAddRequest authUserAddRequest) {
//        log.info("email : {}", authUserAddRequest.getEmail());
//        log.info("password : {}", authUserAddRequest.getPassword());
        if (authUserRepository.findByEmail(authUserAddRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//            return ResponseEntity.status(409).build();
        }

        authUserService.save(authUserAddRequest);
        return ResponseEntity.ok().build();
    }

    // 로그인 성공시 동작할 메서드. 브라우저에 리프레시, 액세스 토큰 모두 전달
    @GetMapping("/login/{id}")
    public ResponseEntity<AuthUserLoginResponse> loginSuccessAuthentication(
            @PathVariable String id,
            HttpServletRequest request
    ) {

        // 로컬 환경과 달리 서버 환경에서는 이 시점에서 authentication의 값이 null. (loadUserByUsername에서는 잘 리턴함)
        // 실패지점 추측으로 서버 환경에서는 1. SecurityContext에 사용자정보를 처음부터 저장을 안하거나(마치 SessionCreationPolicy.STATELESS)
        // 2. 필터체인 실행과 api 실행이 각각 별개 실행으로 간주해서 SecurityContext 정보가 계속 이어지지 않거나
        // => 코드로 확인해보니 2번이 맞다.
        // => 해결책: 필터체인에서 successHandler 객체에서 SecurityContext값을 여기 api로 param값(id)으로 보내와서 사용
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        System.out.println("(loginSuccess)authentication.getName(): " + email);

//        System.out.println("(loginSuccess)id = " + id);
//        System.out.println("request.getRequestURL() = " + request.getRequestURL());
//        System.out.println("request.getRequestURI() = " + request.getRequestURI());

//        System.out.println("request.getParameterNames() = " + request.getParameterNames());
//        Enumeration<String> paramKeys = request.getParameterNames();
//        while (paramKeys.hasMoreElements()) {
//            String key = paramKeys.nextElement();
//            System.out.println(key+":"+request.getParameter(key));
//        }

//        System.out.println("@PathVariable email = " + email);

//        AuthUser authuser = authUserRepository.findByEmail(email);
        AuthUser authuser = authUserRepository.findByEmail(id);

        // 로그인할때 리프레시 토큰과 액세스 토큰 생성
        String refreshToken = tokenService.createNewRefreshToken(authuser);
        String accessToken = tokenService.createNewAccessToken(authuser, refreshToken);

        // Redis에 리프레시 토큰 저장
//        RefreshToken redis = new RefreshToken(refreshToken, authuser.getId());
//        refreshTokenRepository.save(redis);

        // 일반 DB 저장
        RefreshToken rt = new RefreshToken(authuser.getId(), refreshToken);
        refreshTokenRepository.save(rt);

        // 리프레시 토큰으로 ID 찾는 메서드를 Redis에 캐싱
        tokenService.getAuthUserIdRefreshToken(refreshToken);

        // 현재 스프링에서 받아온 쿠키는 새로고침하면 사라진다.. => 일단 바디로 보내서 리액트 훅으로 setCookie
//        Cookie cookie = new Cookie("refresh_token", refreshToken);
//        response.addCookie(cookie);

        // 이메일, 리프레시 토큰, 액세스 토큰 전달
        return ResponseEntity.ok().body(new AuthUserLoginResponse(authuser.getEmail(), refreshToken, accessToken));
    }

    // 로그인 실패시 401 반환 => 실패핸들러에서 sendError()로 처리
//    @GetMapping("/unauthorized")
//    public ResponseEntity<Void> loginFailureUnauthorized(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
//        return ResponseEntity.status(401).build();
//    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
//                                         ,@RequestHeader("Authorization") String refreshToken) {
        // Redis에서 토큰도 삭제?
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().build();
    }

}
