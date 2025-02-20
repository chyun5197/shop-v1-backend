package project.shopclone.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import project.shopclone.domain.user.service.AuthUserDetailService;
import project.shopclone.global.jwt.TokenAuthenticationFilter;
import project.shopclone.global.jwt.service.TokenProvider;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthUserDetailService authUserDetailService;
    private final TokenProvider tokenProvider;

    // 해당 리소스에 대한 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure(){
//        return (web) -> web.ignoring() // 시큐리티 사용 비활성화
//                .requestMatchers(new AntPathRequestMatcher("/static/**")); // static 하위 리소스 제외
//    }



    //특정 http 요청에 대한 웹 기반 보안 구성 : 해당 메서드에서 인증/인가 및 로그인, 로그아웃 관련 설정한다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable) // csrf 해제
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화 (토큰 방식으로 대체하려면 사용)
//                .formLogin(AbstractHttpConfigurer::disable) // 폼로그인 비활성화 기능
//                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                -> sessionManagement는 인증 정보를 SecurityContextHolder(Authentication활용)에 저장해놓을지 말지. STATELESS면 인증 정보 저장X
//                -> 현재의 폼로그인에서는 off
//                -> 로그인 성공시 /login 컨트롤러에서 사용자 아이디를 SecurityContextHolder로부터 꺼내와서 토큰 만들도록 짜놓았기 때문
//                -> !소셜로그인 구현했을때는 구글(카카오)에서 사용자 정보(이메일)을 받아온걸로 토큰 만드니까 SecurityContextHolder가 필요 없었음
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth //인증, 인가 설정
                        .requestMatchers( // 인증 필요
                                 "/api/cart/**", "/api/wish/**", "/api/member/**"
//                                new AntPathRequestMatcher("/signup")
                        ).authenticated()
                        .anyRequest().permitAll()) // 그외 api는 인증없이 통과
                .formLogin(formLogin -> formLogin // 폼 기반 로그인 설정
    //                        .loginPage("/login") // 인증이 필요할 때 이동하는 페이지 설정하는 api.
    //                        해당 api설정을 하지 않을 경우 기본적으로 스프링 시큐리티가 제공하는 템플릿으로 연결
    //                        로그인 페이지 리액트에서 만들것이기에 여기서는 주석처리
                                .loginProcessingUrl("/login-form")    // 실제 로그인 처리 엔드 포인트 (로그인 form POST Action URL)
                                .usernameParameter("email") // form에서 email로 유저 이름 사용 사용
                                .passwordParameter("password") // 패스워드 파라미터 설정
                                // defaultSuccessUrl 와 successHandler 중 하나만 선택 (둘 다 작성하면 핸들러를 실행)
                                // 1) defaultSuccessUrl: 작성한 url 실행 (스프링 api 컨트롤러 실행 가능 or 리액트주소/경로 페이지 라우팅 가능)
                                // 2) successHandler : 클래스 구현해서 사용가능 + url 실행 (sendRedirect()가 url 실행) ~ 소셜 로그인 구현했을때 사용함(OAuth2SuccessHandler.class)
//                                .defaultSuccessUrl("/api/user/login") // 인증이 성공하였을 때 이동할 url
                                .failureHandler( // defaultFailureUrl은 따로 없음
                                        new AuthenticationFailureHandler() {
                                            @Override
                                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                                // exception이 BadCredentialsException인지 UsernameNotFoundException인지에 따라 아이디/비번 무엇이 틀렸는지 찾을수 있을듯
                                                // 여기서 BadCredentialsException ~ UsernamePasswordAuthenticationFilter 비밀번호
                                                // loadUserByUsername()에서 UsernameNotFoundException 아이디
                                                // 상태코드 다르게 보내서 리액트에서 error.status로 경우 나눠어서 찾기 가능
//                                                System.out.println("exception: "+ exception);
//                                                System.out.println("상태코드: " +response.getStatus());

//                                                System.out.println("실패 메세지: " + exception.getMessage());
                                                response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
//                                                response.sendRedirect("/api/user/unauthorized"); // 아이디/비번 어떤게 틀렸는지까지 보내고 싶다면 추가 작성
                                            }
                                        }
                                )
                                .successHandler(
                                        new AuthenticationSuccessHandler() {
                                            @Override
                                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                                System.out.println("(필터체인)authentication : " + authentication.getName());
                                                response.sendRedirect("/api/user/login/" + authentication.getName()); // 인증이 성공한 후에는 root로 이동
                                            }
                                        }
                                )
                )
                .logout(logout -> logout // 로그아웃 설정
//                        .logoutSuccessUrl("/") // 로그아웃 시 이동 URL
                        .invalidateHttpSession(true) // 로그아웃 이후 세션을 전체 삭제할지 여부
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                                // /api로 시작하는 url인 경우 401 상태 코드를 반환하도록 예외 처리
                                // 로그인 안하고 개인정보 관련 api 호출할 때
                        ))
                .build();

    }

    // 인증 관리자 관련 설정
    // 사용자 정보를 가져올 서비스를 재정희거나,
    // 인증 방법 ~ 예를 들어 LDAP, JDBC 기반 인증 등을 설정할 때 사용
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       AuthUserDetailService authUserDetailService) throws Exception{
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authUserDetailService); // 사용자 정보 서비스 설정(UserDetailsService 상속 받은 클래스)
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호 암호화하기 위한 인코더 설정
        return new ProviderManager(authProvider);
    }

    //패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5174");// 리액트 서버
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }
}
