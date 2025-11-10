package server.dongmin.domain.basket.dto.req;

import jakarta.validation.constraints.NotBlank;

public record BasketItemRequest(

        @NotBlank(message = "메뉴 ID가 비어있습니다.")
        Long menuId,

        @NotBlank(message = "수량이 비어있습니다.")
        int quantity
) {

}
