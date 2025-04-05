package project.shopclone.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.shopclone.domain.member.entity.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {
    private String name;
    private String address;
    private String phone;
    private Integer savings; // 적립금

    private Long memberId;
    private String email;  // member.email을 아이디로 사용

    public static MemberInfoResponse from(Member member) {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        memberInfoResponse.setMemberId(member.getMemberId());
        memberInfoResponse.setName(member.getName());
        memberInfoResponse.setAddress(member.getAddress());
        memberInfoResponse.setPhone(member.getPhone());
        memberInfoResponse.setSavings(member.getSavings());

        memberInfoResponse.setEmail(member.getEmail());
        return memberInfoResponse;
    }
}
