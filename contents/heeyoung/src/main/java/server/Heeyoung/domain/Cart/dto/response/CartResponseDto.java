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

    public static CartResponseDto from(Cart cart, List<CartMenuDto> cartMenuList) {
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .cartMenuList(cartMenuList)
                .storeId(cart.getStore().getId())
                .build();
    }
}
