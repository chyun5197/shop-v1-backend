package project.shopclone.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import project.shopclone.domain.user.AuthUser;
import project.shopclone.global.jwt.JwtProperties;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
// 토큰을 생성하고 올바른 토큰인지 유효성 검사를 하고, 토큰에서 필요한 정보를 가져오는 클래스
public class TokenProvider {
    private final JwtProperties jwtProperties;

    // 토큰 생성
    public String generateToken(AuthUser authUser, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), authUser);
    }

    // AuthUser로 JWT 토큰 생성
    private String makeToken(Date expiry, AuthUser authUser) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ: JWT
                .setIssuer(jwtProperties.getIssuer()) // 내용 iss: yml에서 설정한 발급자 issuer (현재 이메일)
                .setIssuedAt(now)       // 내용 iat: 현재 시간
                .setExpiration(expiry)  // 내용 exp: expiry 멤버 변수값
                .setSubject(authUser.getEmail()) // 내용 sub: 유저의 이메일
                .claim("id", authUser.getId()) // 클레임 id: 유저 ID
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) //서명: secretKey와 함께 해시값을 HS256 방식으로 암호화
                .compact();
    }

    // (캐싱) JWT 토큰 유효성 검증
//    @Cacheable(cacheNames = "isValidToken", key="'validToken:' + #token")
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // secretKey로 복호화
                    .parseClaimsJws(token);
            return true;
        }catch(Exception e){ // 복호화 과정에서 에러나면 유효하지 않은 토큰
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오기
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }

    // 토큰 기반으로 AuthUser ID를 가져오기
    public Long getAuthUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // 클레임 정보 조회
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
