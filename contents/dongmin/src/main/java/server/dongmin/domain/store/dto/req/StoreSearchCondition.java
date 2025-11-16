package server.dongmin.domain.store.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import server.dongmin.domain.store.enums.StoreCategory;

@Schema(description = "가게 정보 조회 필터링 조건")
public record StoreSearchCondition(

        @Schema(description = "장소", example = "인천광역시")
        String location,

        @Schema(description = "카테고리", example = "FASTFOOD")
        StoreCategory category
        // 필터링 조건 추가 가능
) {
}
