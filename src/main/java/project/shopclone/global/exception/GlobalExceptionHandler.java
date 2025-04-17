package project.shopclone.global.exception;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.shopclone.domain.order.exception.OrderException;
import project.shopclone.domain.user.exception.AuthUserException;
import project.shopclone.domain.wish.exception.WishException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<ErrorRes<Void>> memberExceptionHandler(AuthUserException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorRes<Void>> orderExceptionHandler(OrderException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(WishException.class)
    public ResponseEntity<ErrorRes<Void>> wishExceptionHandler(WishException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorRes.failure(e.getCode(), e.getMessage()));
    }

//    @ExceptionHandler(IamportResponseException.class)
//    public ResponseEntity<String> iamportResponseException(final IamportResponseException e) {
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }
}
