package project.shopclone.domain.wish.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WishException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public WishException(WishErrorCode authUserErrorCode) {
        super(authUserErrorCode.getMessage());
        this.httpStatus = authUserErrorCode.getHttpStatus();
        this.code = authUserErrorCode.getCode();
    }
}
