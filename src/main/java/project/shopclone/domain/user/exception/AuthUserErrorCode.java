package project.shopclone.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import project.shopclone.global.exception.BaseError;

@Getter
@AllArgsConstructor
public enum AuthUserErrorCode implements BaseError {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AuthUser_404", "회원을 찾을 수 없습니다"),
    USER_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "AuthUser_406", "현재 접속한 사용자와 회원ID가 일치하지 않습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
