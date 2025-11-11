package server.Heeyoung.domain.Menu.Dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private String menuName;

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private Long price;

    private String menuPicture;
}
