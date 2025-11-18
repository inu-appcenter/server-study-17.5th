package server.Heeyoung.domain.CartMenu.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartMenuRequestDto {

    @NotNull(message = "메뉴 ID 는 필수입니다.")
    @Schema(description = "메뉴 ID", example = "5")
    private Long menuId;

    @NotNull(message = "가게 ID 는 필수입니다.")
    @Schema(description = "가게 ID", example = "1")
    private Long storeId;

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 1 이상이어야 합니다.")
    @Schema(description = "장바구니 메뉴 수량", example = "2")
    private Long cartMenuQuantity;

}
