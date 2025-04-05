package project.shopclone.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.domain.user.dto.request.AuthUserAddRequest;

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
                .oauthId(authUserAddRequest.getEmail())
                .email(authUserAddRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(authUserAddRequest.getPassword()))
                .channel("home")
                .build();
        authUserRepository.save(authUser);

        // Member 생성
        Member member = memberService.save(authUser, authUserAddRequest);
    }

//    public AuthUser findById(Long id) {
//        return authUserRepository.findById(id)
//                .orElseThrow(() -> new AuthUserException(AuthUserErrorCode.USER_NOT_FOUND));
//    }

}
