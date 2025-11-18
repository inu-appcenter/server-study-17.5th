package server.Heeyoung.domain.Menu.Dto.RequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    @Schema(description = "메뉴 이름", example = "떡꼬치")
    private String menuName;

    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    @Schema(description = "메뉴 가격", example = "2000")
    private Long price;

    @Schema(description = "메뉴 사진 파일명", example = "tteokkochi.jpg")
    private String menuPicture;
}
