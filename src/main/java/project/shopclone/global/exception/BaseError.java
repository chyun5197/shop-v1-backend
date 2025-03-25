package project.shopclone.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseError {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
