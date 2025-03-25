package project.shopclone.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorRes<T> {
    private String code;
    private String message;

    public static <T> ErrorRes<T> failure(String code, String message) {
        return ErrorRes.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}
