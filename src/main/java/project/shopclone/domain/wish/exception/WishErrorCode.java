package project.shopclone.domain.wish.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import project.shopclone.global.exception.BaseError;

@Getter
@AllArgsConstructor
public enum WishErrorCode implements BaseError {
    WISH_ALREADY_REPORTED(HttpStatus.ALREADY_REPORTED, "WISH_208", "이미 등록된 위시 상품입니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
