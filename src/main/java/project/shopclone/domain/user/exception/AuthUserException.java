package project.shopclone.domain.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthUserException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public AuthUserException(AuthUserErrorCode authUserErrorCode) {
        super(authUserErrorCode.getMessage());
        this.httpStatus = authUserErrorCode.getHttpStatus();
        this.code = authUserErrorCode.getCode();
    }
}
