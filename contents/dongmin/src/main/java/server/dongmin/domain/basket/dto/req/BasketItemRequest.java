package server.dongmin.domain.basket.dto.req;

import jakarta.validation.constraints.NotNull;

public record BasketItemRequest(

        @NotNull(message = "메뉴 ID가 비어있습니다.")
        Long menuId,

        @NotNull(message = "수량이 비어있습니다.")
        Integer quantity
) {

}
