package server.dongmin.domain.basket.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import server.dongmin.domain.basket.entity.BasketItem;

import java.math.BigDecimal;

@Schema(description = "장바구니 품목 정보 응답 DTO")
public record BasketItemResponse(
        @Schema(description = "장바구니 ID", example = "1")
        Long basketItemId,

        @Schema(description = "장바구니 품목 ID", example = "1")
        Long menuId,

        @Schema(description = "장바구니 품목 수량", example = "1")
        int quantity,

        @Schema(description = "장바구니 품목 단일 가격", example = "1000")
        BigDecimal price, // 단일 가격

        @Schema(description = "수량 X 단일 가격", example = "1000")
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
