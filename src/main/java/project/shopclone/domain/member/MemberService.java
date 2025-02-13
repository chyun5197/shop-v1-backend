package project.shopclone.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.cart.repository.CartRepository;
import project.shopclone.domain.cart.entity.Cart;
import project.shopclone.domain.user.AuthUser;
import project.shopclone.domain.user.AuthUserRepository;
import project.shopclone.global.jwt.service.TokenProvider;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthUserRepository authUserRepository;
    private final CartRepository cartRepository;

    // 처음 정보 저장
    @Transactional
    public Member save(AuthUser authUser) {
        // 멤버 생성
        Member member = memberRepository.save(Member.builder()
                .email(authUser.getEmail())
                .authUser(authUser)
                .build());

        // 카트 생성
        cartRepository.save(Cart.builder()
                .member(member)
                .build());

        return member;
    }

    // 장바구니에 추가하면 Member의 필드값(cartCount)가 변하니 기존 캐시된 Member 객체를 활용하면 오류남
//    @Cacheable(cacheNames = "member", key="'accessToken:' + #accessToken.split(\" \")[1]", cacheManager = "redisCacheManager")
    public Member getMember(String accessToken){
        Long authUserId = tokenProvider.getAuthUserId(accessToken.split(" ")[1]);
        return memberRepository.findByAuthUser(authUserRepository.findById(authUserId).orElseThrow());
    }

    // ========================================================================================================================
    // 캐시된 ID는 Long이 아닌 Integer로 반환되는 문제. 역직렬화는 제공 안하는지?
//    @Cacheable(cacheNames = "memberId", key="'accessToken:' + #accessToken.split(\" \")[1]", cacheManager = "redisCacheManager")
    public Long getMemberId(String accessToken){
        Long authUserId = tokenProvider.getAuthUserId(accessToken.split(" ")[1]);
        return memberRepository.findByAuthUser(authUserRepository.findById(authUserId).orElseThrow()).getMemberId();
    }


//    public Member getMember(Long id){
//        return memberRepository.findById(id).orElseThrow();
//    }


}
