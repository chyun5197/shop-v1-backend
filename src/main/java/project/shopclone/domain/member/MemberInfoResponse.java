package project.shopclone.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {
    private String email;
    private String name;
    private String address;
    private String phone;

    private Integer wishCount;
    private Integer cartCount;
    private Integer savings; // 적립금

    private String oauthId;

    public static MemberInfoResponse from(Member member) {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        memberInfoResponse.setEmail(member.getEmail());
        memberInfoResponse.setName(member.getName());
        memberInfoResponse.setAddress(member.getAddress());
        memberInfoResponse.setPhone(member.getPhone());

        memberInfoResponse.setWishCount(member.getWishCount());
        memberInfoResponse.setCartCount(member.getCartCount());
        memberInfoResponse.setSavings(member.getSavings());
        memberInfoResponse.setOauthId(member.getAuthUser().getOauthId());
        return memberInfoResponse;
    }
}
