package server.Heeyoung.domain.CartMenu.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Heeyoung.domain.CartMenu.Service.CartMenuService;
import server.Heeyoung.domain.CartMenu.dto.request.CartMenuRequestDto;
import server.Heeyoung.domain.CartMenu.dto.response.CartMenuResponseDto;

@RestController
@RequestMapping("/cart/items")
@RequiredArgsConstructor
public class CartMenuController {

    private final CartMenuService cartMenuService;

    // 장바구니에 메뉴 추가
    @PostMapping
    public ResponseEntity<CartMenuResponseDto> addMenuToCart(
            @RequestParam Long userId,
            @RequestParam Long storeId,
            @RequestBody CartMenuRequestDto dto
    ) {
        CartMenuResponseDto response = cartMenuService.addMenuToCart(dto, userId, storeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니에 메뉴 수량 수정
    @PutMapping("/{cartMenuId}")
    public ResponseEntity<CartMenuResponseDto> updateCartMenuQuantity(
            @PathVariable("cartMenuId") Long cartMenuId,
            @RequestParam Long userId,
            @RequestParam Long newQuantity
    ) {
        CartMenuResponseDto response = cartMenuService.updateCartMenuQuantity(userId, cartMenuId, newQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장바구니 메뉴 삭제
    @DeleteMapping("/{cartMenuId}")
    public ResponseEntity<String> deleteCartMenu(
            @PathVariable("cartMenuId") Long cartMenuId,
            @RequestParam Long userId
    ) {
        cartMenuService.deleteCartMenu(userId, cartMenuId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 메뉴가 삭제되었습니다.");
    }

}
