package project.shopclone.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.shopclone.domain.cart.entity.Cart;
import project.shopclone.domain.cart.repository.CartRepository;
import project.shopclone.domain.member.dto.MemberUpdateRequest;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.repository.MemberRepository;
import project.shopclone.domain.user.repository.AuthUserRepository;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    AuthUserRepository authUserRepository;
    @Mock
    CartRepository cartRepository;
    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateTest(){
        // given
        Long memberId = 1L;
        Member member = createMember(memberId);
        MemberUpdateRequest memberUpdateRequest = MemberUpdateRequest.builder()
                .name("hyun")
                .address("서울시 서초구")
                .build();

        // when
        memberService.updateMemberInfo(member,memberUpdateRequest);

        // then
        verify(member).updateMember(memberUpdateRequest);
    }

    private Member createMember(Long memberId){
        Member member = mock(Member.class);
        given(member.getMemberId()).willReturn(memberId);
        return member;
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void signOutTest(){
        // given
        Long memberId = 1L;
        Member member = mock(Member.class);
        Cart cart = mock(Cart.class);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(cartRepository.findByMember(member)).willReturn(cart);

        // when
        memberService.signOut(member, 1L);

        // then
        verify(cartRepository).delete(cart);
        verify(authUserRepository).delete(member.getAuthUser());
    }

}