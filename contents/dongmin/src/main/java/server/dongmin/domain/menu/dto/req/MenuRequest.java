package server.dongmin.domain.menu.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "메뉴 생성 요청 DTO")
public record MenuRequest(

        @Schema(description = "메뉴 이름", example = "불고기버거")
        @NotNull(message = "메뉴 이름이 비어있습니다.")
        String menuName,

        @Schema(description = "가격", example = "4000")
        @NotNull(message = "가격이 비어있습니다.")
        BigDecimal price,

        @Schema(description = "설명", example = "대표 메뉴")
        String content,

        @Schema(description = "카테고리", example = "버거")
        @NotBlank(message = "카테고리가 비어있습니다.")
        String category
) {

}
