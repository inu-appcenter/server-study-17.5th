package server.Heeyoung.domain.CartMenu.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.Heeyoung.domain.CartMenu.entity.CartMenu;

@Getter
@Builder
public class CartMenuResponseDto {

    private final Long cartMenuId;

    private final String menuName;

    private final Long quantity;

    private final Long price;

    public static CartMenuResponseDto from(CartMenu cartMenu) {
        return CartMenuResponseDto.builder()
                .cartMenuId(cartMenu.getId())
                .menuName(cartMenu.getMenu().getMenuName())
                .quantity(cartMenu.getCartMenuQuantity())
                .price(cartMenu.getMenu().getPrice())
                .build();
    }

}
