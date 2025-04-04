package project.shopclone.domain.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import project.shopclone.global.exception.BaseError;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseError {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order_404", "주문 내역을 찾을 수 없습니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Payment_404", "결제 내역을 찾을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
