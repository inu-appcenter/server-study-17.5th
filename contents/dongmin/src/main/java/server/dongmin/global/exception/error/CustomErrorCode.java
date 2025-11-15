package server.dongmin.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // User
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 401, "이메일 또는 비밀번호가 일치하지 않습니다."),
    USER_BANNED(HttpStatus.FORBIDDEN, 403, "정지된 사용자 입니다."),
    USER_NOT_CREATE_STORE(HttpStatus.FORBIDDEN, 403, "가게를 만들 수 없는 유저입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404,"존재하지 않는 유저입니다"),
    EMAIL_CONFLICT(HttpStatus.CONFLICT, 409, "이미 가입된 이메일 입니다."),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, 409, "이미 존재하는 별명 입니다."),
    NUMBER_CONFLICT(HttpStatus.CONFLICT, 409, "이미 가입된 번호 입니다."),

    // Basket
    DIFFERENT_STORE_IN_BASKET(HttpStatus.BAD_REQUEST, 400, "장바구니에는 같은 가게의 상품만 담을 수 있습니다."),

    // Menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, 404,"존재하지 않는 메뉴입니다"),

    // Order

    // Store
    STORE_OWNER_MISMATCH(HttpStatus.FORBIDDEN, 403, "가게 주인이 아닙니다. 수정할 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, 404,"존재하지 않는 가게입니다"),

    // API
    HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, ""),

    // Validation
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 400, ""),

    // JWT
    JWT_USER_MISMATCH(HttpStatus.UNAUTHORIZED, 401, "토큰의 사용자가 일치하지 않습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, 401, "만료된 엑세스 토큰입니다."),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, 401, "잘못된 토큰 형식입니다."),
    JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 서명입니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, 401, "지원하지 않는 토큰입니다."),
    JWT_NOT_MATCH(HttpStatus.BAD_REQUEST, 400, "리프레시 토큰 불일치"),
    JWT_ENTRY_POINT(HttpStatus.UNAUTHORIZED, 401, "인증되지 않은 사용자입니다."),
    JWT_ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "리소스에 접근할 권한이 없습니다."),
    JWT_REFRESH_NOT_FOUND(HttpStatus.NOT_FOUND, 404,"리프레시 토큰이 존재하지 않습니다."),
    JWT_ERROR(HttpStatus.UNAUTHORIZED, 401, "JWT 관련 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

}
