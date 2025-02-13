package project.shopclone.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberService;
import project.shopclone.domain.user.AuthUser;
import project.shopclone.domain.user.AuthUserRepository;
import project.shopclone.domain.user.service.request.AuthUserAddRequest;
import project.shopclone.global.jwt.refreshtoken.RefreshToken;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenService;

@RequiredArgsConstructor
@Service
public class AuthUserService {
    private final AuthUserRepository authUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberService memberService;



    // 회원가입
    @Transactional
    public void save(AuthUserAddRequest authUserAddRequest) {
        // AuthUser 생성
        AuthUser authUser = AuthUser.builder()
                .email(authUserAddRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(authUserAddRequest.getPassword()))
                .build();
        authUserRepository.save(authUser);

        // Member 생성
        Member member = memberService.save(authUser);
    }

    public AuthUser findById(Long id) {
        return authUserRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

}
