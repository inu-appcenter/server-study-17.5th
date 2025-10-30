package server.Heeyoung.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),
    DIFFERENT_STORE_CART_EXISTS(HttpStatus.BAD_REQUEST, "다른 가게의 장바구니가 이미 존재합니다."),
    CART_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 메뉴를 찾을 수 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "잘못된 수량입니다."),
    UNAUTHORIZED_CART_ACCESS(HttpStatus.FORBIDDEN, "본인의 장바구니만 수정할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getCode() {
        return this.name();
    }
}
