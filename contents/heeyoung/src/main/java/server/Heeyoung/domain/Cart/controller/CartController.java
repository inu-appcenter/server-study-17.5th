package server.Heeyoung.domain.Cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Heeyoung.domain.Cart.dto.response.CartResponseDto;
import server.Heeyoung.domain.Cart.service.CartService;
import server.Heeyoung.domain.CartMenu.Service.CartMenuService;
import server.Heeyoung.domain.CartMenu.dto.request.CartMenuRequestDto;
import server.Heeyoung.domain.CartMenu.dto.response.CartMenuResponseDto;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
// 유저 관련 정보는 spring security 로 받아오기
public class CartController {

    private final CartService cartService;
    private final CartMenuService cartMenuService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@RequestParam Long userId) {
        CartResponseDto response = cartService.findCart(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestParam Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.ok().build();
    }

    // 장바구니에 메뉴 추가
    @PostMapping
    public ResponseEntity<CartMenuResponseDto> addMenuToCart(
            @RequestParam Long userId, // spring security 로 수정예정
            @RequestParam Long storeId,
            @RequestBody CartMenuRequestDto dto
    ) {
        CartMenuResponseDto response = cartMenuService.addMenuToCart(dto, userId, storeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 메뉴 수량 수정
    @PatchMapping("/{cartMenuId}")
    public ResponseEntity<CartMenuResponseDto> updateCartMenuQuantity(
            @PathVariable Long cartMenuId,
            @RequestParam Long userId, // spring security 로 수정예정
            @RequestParam Long newQuantity
    ) {
        CartMenuResponseDto response = cartMenuService.updateCartMenuQuantity(userId, cartMenuId, newQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 메뉴 삭제
    @DeleteMapping("/{cartMenuId}")
    public ResponseEntity<String> deleteCartMenu(
            @PathVariable Long cartMenuId,
            @RequestParam Long userId
    ) {
        cartMenuService.deleteCartMenu(userId, cartMenuId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 메뉴가 삭제되었습니다.");
    }
}
