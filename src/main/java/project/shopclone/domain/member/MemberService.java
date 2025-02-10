package project.shopclone.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.user.AuthUser;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // 처음 정보 저장
    @Transactional
    public Member save(AuthUser authUser) {
        return memberRepository.save(Member.builder()
                .email(authUser.getEmail())
                .authUser(authUser)
                .build());
    }
}
