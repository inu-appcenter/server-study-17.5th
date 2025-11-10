package server.dongmin.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final Integer code;
    private final String message;

    // Exception에 맞는 ErrorResponse 생성
    public static ErrorResponse create(Integer code, String message) {
        return new ErrorResponse(code, message);
    }
}
