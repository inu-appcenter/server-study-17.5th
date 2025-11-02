package server.dongmin.domain.basket.dto.res;

import server.dongmin.domain.basket.entity.BasketItem;

import java.math.BigDecimal;

public record BasketItemResponse(
        Long basketItemId,
        Long menuId,
        int quantity,
        BigDecimal price, // 단일 가격
        BigDecimal totalPrice // 수량 * 가격
) {
    public static BasketItemResponse from(BasketItem basketItem) {
        return new BasketItemResponse(
                basketItem.getBasketItemId(),
                basketItem.getMenuId(),
                basketItem.getQuantity(),
                basketItem.getPrice(),
                basketItem.getPrice().multiply(BigDecimal.valueOf(basketItem.getQuantity()))
        );
    }
}
