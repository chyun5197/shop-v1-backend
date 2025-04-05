package project.shopclone.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberUpdateRequest {
    private String email;
    private String name;
    private String address;
    private String phone;
}
