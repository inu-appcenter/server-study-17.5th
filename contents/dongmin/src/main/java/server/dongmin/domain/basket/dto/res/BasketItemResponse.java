package server.dongmin.domain.basket.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import server.dongmin.domain.basket.entity.BasketItem;
import server.dongmin.domain.menu.entity.Menu;

import java.math.BigDecimal;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BasketItemResponse {

    private Long basketItemId;
    private Long menuId;
    private int quantity;
    private BigDecimal price; // 단일 가격
    private BigDecimal totalPrice; // 수량 * 가격

    public static BasketItemResponse from(BasketItem basketItem, Menu menu) {
        BigDecimal price = menu.getPrice();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(basketItem.getQuantity()));
        return BasketItemResponse.builder()
                .basketItemId(basketItem.getBasketItemId())
                .menuId(basketItem.getMenuId())
                .quantity(basketItem.getQuantity())
                .price(price)
                .totalPrice(totalPrice)
                .build();
    }
}
