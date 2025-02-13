package project.shopclone.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import project.shopclone.domain.user.service.AuthUserDetailService;
import project.shopclone.global.jwt.TokenAuthenticationFilter;
import project.shopclone.global.jwt.service.TokenProvider;

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
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화 ( 토큰 방식으로 대체하려면 사용 )
//                .formLogin(AbstractHttpConfigurer::disable) // 폼로그인 비활성화
//                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                -> sessionManagement는 인증 정보를 SecurityContextHolder(Authentication활용)에 저장해놓을지 말지. STATELESS면 인증 정보 저장X
//                -> JWT와 같이 세션을 사용하지 않을 경우에 사용
//                -> 세션매니먼트 키고 /api/profile 요청은 받아서 로그인중인 사용자정보 잘 받아오는거 확인 (profile.js로 프론트에서 토큰 전송)
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
                                .defaultSuccessUrl("/api/user/login") // 반환할 스프링 rest URL
                )
                .logout(logout -> logout // 로그아웃 설정
//                        .logoutSuccessUrl("/") // 로그아웃 시 이동 URL
                        .invalidateHttpSession(true) // 로그아웃 이후 세션을 전체 삭제할지 여부
                )
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
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
