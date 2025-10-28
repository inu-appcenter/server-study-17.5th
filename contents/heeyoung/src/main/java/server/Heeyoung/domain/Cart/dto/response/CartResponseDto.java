package server.Heeyoung.domain.Cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.Heeyoung.domain.Cart.entity.Cart;
import server.Heeyoung.domain.CartMenu.entity.CartMenu;
import server.Heeyoung.domain.Store.entity.Store;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CartResponseDto {

    private final Long cartId;

    private final List<CartMenuDto> cartMenuList;

    private final Long storeId;

    public static CartResponseDto from(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .cartMenuList(
                        cart.getCartMenuList().stream()
                                .map(cm -> CartMenuDto.builder()
                                        .menuName(cm.getMenu().getMenuName())
                                        .quantity(cm.getCartMenuQuantity())
                                        .build())
                                .collect(Collectors.toList())
                )
                .storeId(cart.getStore().getId())
                .build();
    }


}
