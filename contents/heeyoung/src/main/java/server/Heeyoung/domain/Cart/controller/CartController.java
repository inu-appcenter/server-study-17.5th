package server.Heeyoung.domain.Cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.Heeyoung.domain.Cart.dto.response.CartResponseDto;
import server.Heeyoung.domain.Cart.service.CartService;
import server.Heeyoung.domain.CartMenu.Service.CartMenuService;
import server.Heeyoung.domain.CartMenu.dto.request.CartMenuRequestDto;
import server.Heeyoung.domain.CartMenu.dto.response.CartMenuResponseDto;
import server.Heeyoung.domain.User.Service.UserDetailsImpl;
import server.Heeyoung.global.exception.RestApiException;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMenuService cartMenuService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponseDto response = cartService.findCart(userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteCart(userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }

    // 장바구니에 메뉴 추가
    @PostMapping
    public ResponseEntity<CartMenuResponseDto> addMenuToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CartMenuRequestDto dto
    ) {
        CartMenuResponseDto response = cartMenuService.addMenuToCart(dto, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 메뉴 수량 수정
    @PatchMapping("/{cartMenuId}")
    public ResponseEntity<CartMenuResponseDto> updateCartMenuQuantity(
            @PathVariable Long cartMenuId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long newQuantity
    ) {
        CartMenuResponseDto response = cartMenuService.updateCartMenuQuantity(userDetails.getUser().getId(), cartMenuId, newQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 메뉴 삭제
    @DeleteMapping("/{cartMenuId}")
    public ResponseEntity<String> deleteCartMenu(
            @PathVariable Long cartMenuId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cartMenuService.deleteCartMenu(userDetails.getUser().getId(), cartMenuId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 메뉴가 삭제되었습니다.");
    }
}
