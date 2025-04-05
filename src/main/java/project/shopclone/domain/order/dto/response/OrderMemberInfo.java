package project.shopclone.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.member.entity.Member;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderMemberInfo { // 주문자 정보
    private String email;
    private String name;
    private String phone;
    public static OrderMemberInfo from(Member member){
        OrderMemberInfo memberInfo = new OrderMemberInfo();
        memberInfo.email = member.getEmail();
        memberInfo.name = member.getName();
        memberInfo.phone = member.getPhone();
        return memberInfo;
    }
}
