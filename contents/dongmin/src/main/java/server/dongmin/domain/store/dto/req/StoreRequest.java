package server.dongmin.domain.store.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

@Schema(description = "가게 생성 요청 DTO")
public record StoreRequest(

        @Schema(description = "가게 이름", example = "롯데리아 청라호수공원점")
        @NotBlank(message = "가게 이름이 비어있습니다.")
        String storeName,

        @Schema(description = "카테고리", example = "FASTFOOD")
        @NotNull(message = "카테고리가 비어있습니다.")
        StoreCategory category,

        @Schema(description = "설명", example = "호수공원에 위치한 가게")
        String content,

        @Schema(description = "주소", example = "인천 서구 솔빛로 88")
        @NotBlank(message = "주소가 비어있습니다.")
        String address,

        @Schema(description = "최소 배달 금액", example = "10000")
        @NotNull(message = "최소 배달 금액이 비어있습니다.")
        BigDecimal minDeliveryPrice,

        @Schema(description = "배달 팁", example = "2000")
        @NotNull(message = "배달 팁이 비어있습니다.")
        BigDecimal deliveryTip,

        @Schema(description = "오픈 시간", example = "09:00")
        @NotNull(message = "오픈 시간이 비어있습니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime openBusinessHours,

        @Schema(description = "마감 시간", example = "21:00")
        @NotNull(message = "마감 시간이 비어있습니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime closeBusinessHours
){

}

