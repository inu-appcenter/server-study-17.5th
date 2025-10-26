package server.dongmin.domain.basket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.dongmin.domain.basket.dto.req.BasketItemRequest;
import server.dongmin.domain.basket.dto.res.BasketItemResponse;
import server.dongmin.domain.basket.service.BasketService;
import server.dongmin.domain.user.entity.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/item")
    public ResponseEntity<BasketItemResponse> addBasketItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BasketItemRequest basketItemRequest) { // @RequestBody 추가
        BasketItemResponse response = basketService.addBasketItem(userDetails, basketItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/item")
    public ResponseEntity<Slice<BasketItemResponse>> getBasketItems(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(basketService.getBasketItems(userDetails, pageable));
    }
}
