package server.dongmin.domain.store.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

@Schema(description = "가게 정보 응답 DTO")
public record StoreResponse(

        @Schema(description = "가게 ID", example = "1")
        Long storeId,

        @Schema(description = "가게 주인 이름", example = "owner_name")
        Long userId,

        @Schema(description = "가게 이름", example = "롯데리아 청라호수공원점")
        String storeName,

        @Schema(description = "카테고리", example = "FASTFOOD")
        StoreCategory category,

        @Schema(description = "설명", example = "호수공원에 위치한 가게")
        String content,

        @Schema(description = "주소", example = "인천 서구 솔빛로 88")
        String address,

        @Schema(description = "최소 배달 금액", example = "10000")
        BigDecimal minDeliveryPrice,

        @Schema(description = "배달 팁", example = "2000")
        BigDecimal deliveryTip,

        @Schema(description = "오픈 시간", example = "09:00")
        LocalTime openBusinessHours,

        @Schema(description = "마감 시간", example = "21:00")
        LocalTime closeBusinessHours
){
    public static StoreResponse from(Store store){
        return new StoreResponse(
                store.getStoreId(),
                store.getUserId(),
                store.getStoreName(),
                store.getCategory(),
                store.getContent(),
                store.getAddress(),
                store.getMinDeliveryPrice(),
                store.getDeliveryTip(),
                store.getOpenBusinessHours(),
                store.getCloseBusinessHours()
        );
    }
}
