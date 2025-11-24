package server.dongmin.domain.basket.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "장바구니 품목 생성 요청 DTO")
public record BasketItemRequest(

        @Schema(description = "추가할 메뉴 ID", example = "1")
        @NotNull(message = "메뉴 ID가 비어있습니다.")
        Long menuId,

        @Schema(description = "추가할 메뉴 수량", example = "1")
        @NotNull(message = "수량이 비어있습니다.")
        Integer quantity
) {

}
