package project.shopclone.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN,
    COUNSELOR,
    CUSTOMER,
}

// 열거형 문자열 리턴 차이
// Role.ADMIN.name() -> "ADMIN"
// Role.ADMIN.toString() -> "Admin"