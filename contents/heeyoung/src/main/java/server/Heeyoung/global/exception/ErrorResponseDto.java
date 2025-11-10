package server.Heeyoung.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final String errorCode;
    private final String message;
}
