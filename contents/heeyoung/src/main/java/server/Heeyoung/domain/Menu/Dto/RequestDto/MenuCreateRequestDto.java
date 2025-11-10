package server.Heeyoung.domain.Menu.Dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuCreateRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private final String menuName;

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private final Long price;

    @NotNull
    private final String menuPicture;
}
