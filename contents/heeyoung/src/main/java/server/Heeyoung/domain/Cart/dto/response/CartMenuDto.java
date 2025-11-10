package server.Heeyoung.domain.Cart.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuDto {
    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private final String menuName;

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야합니다.")
    private final Long quantity;
}
