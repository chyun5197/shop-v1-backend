package project.shopclone.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    COUNSELOR("ROLE_COUNSELOR"),
    USER("ROLE_USER");

    private String roleName;
}
