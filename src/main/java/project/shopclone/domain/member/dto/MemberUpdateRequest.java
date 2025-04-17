package project.shopclone.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberUpdateRequest {
    private String name;
    private String address;
    private String phone;
}
