package project.shopclone.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberService;
import project.shopclone.global.jwt.TokenProvider;
import project.shopclone.global.jwt.refreshtoken.RefreshToken;
import project.shopclone.global.jwt.refreshtoken.RefreshTokenRepository;

import static project.shopclone.global.common.TokenDuration.REFRESH_TOKEN_DURATION;

@RequiredArgsConstructor
@Service
public class AuthUserService {
    private final AuthUserRepository authUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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

        // 리프레시 토큰 생성
        String refreshTokenStr = tokenProvider.generateToken(authUser, REFRESH_TOKEN_DURATION);
        refreshTokenRepository.save(new RefreshToken(authUser.getId(), member.getMemberId(), refreshTokenStr));
    }

    public AuthUser findById(Long id) {
        return authUserRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

//    public void test() {
//        System.out.println(securityUserRepository.findByEmail("qwer").get().getEmail());
//    }
}
