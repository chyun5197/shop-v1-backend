package project.shopclone.domain.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import project.shopclone.domain.user.exception.AuthUserErrorCode;

@Getter
public class OrderException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public OrderException(OrderErrorCode orderErrorCode) {
        super(orderErrorCode.getMessage());
        this.httpStatus = orderErrorCode.getHttpStatus();
        this.code = orderErrorCode.getCode();
    }
}
