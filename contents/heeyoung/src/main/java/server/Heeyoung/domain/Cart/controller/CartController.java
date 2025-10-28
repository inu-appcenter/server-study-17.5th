package server.Heeyoung.domain.Cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Heeyoung.domain.Cart.dto.response.CartResponseDto;
import server.Heeyoung.domain.Cart.service.CartService;

@RestController
@RequestMapping("/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long userId) {
        CartResponseDto response = cartService.viewCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@PathVariable Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제되었습니다.");
    }
}
