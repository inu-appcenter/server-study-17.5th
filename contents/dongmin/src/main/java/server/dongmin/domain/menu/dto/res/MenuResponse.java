package server.dongmin.domain.menu.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import server.dongmin.domain.menu.entity.Menu;

import java.math.BigDecimal;

@Schema(description = "메뉴 정보 응답 DTO")
public record MenuResponse(
        @Schema(description = "메뉴 ID", example = "1")
        Long menuId,

        @Schema(description = "가게 ID", example = "1")
        Long storeId,

        @Schema(description = "메뉴 이름", example = "불고기버거")
        String menuName,

        @Schema(description = "가격", example = "4000")
        BigDecimal price,

        @Schema(description = "설명", example = "대표 메뉴")
        String content,

        @Schema(description = "카테고리", example = "버거")
        String category
) {
    public static MenuResponse from(Menu menu) {
        return new  MenuResponse(
                menu.getMenuId(),
                menu.getStoreId(),
                menu.getMenuName(),
                menu.getPrice(),
                menu.getContent(),
                menu.getCategory()
        );
    }
}
