package server.Heeyoung.domain.CartMenu.dto.request;

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
    private Long menuId;

    @NotNull(message = "가게 ID 는 필수입니다.")
    private Long storeId;

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 1 이상이어야 합니다.")
    private Long cartMenuQuantity;

}
