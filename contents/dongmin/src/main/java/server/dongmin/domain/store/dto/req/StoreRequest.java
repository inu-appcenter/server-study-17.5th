package server.dongmin.domain.store.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

public record StoreRequest(

        @NotBlank(message = "가게 이름이 비어있습니다.")
        String storeName,

        @NotNull(message = "카테고리가 비어있습니다.")
        StoreCategory category,

        String content,

        @NotBlank(message = "주소가 비어있습니다.")
        String address,

        @NotNull(message = "최소 배달 금액이 비어있습니다.")
        BigDecimal minDeliveryPrice,

        @NotNull(message = "배달 팁이 비어있습니다.")
        BigDecimal deliveryTip,

        @NotNull(message = "오픈 시간이 비어있습니다.")
        @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
                message = "시간 포맷이 올바르지 않습니다. (예: 10:30)")
        LocalTime openBusinessHours,

        @NotNull(message = "마감 시간이 비어있습니다.")
        @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
                message = "시간 포맷이 올바르지 않습니다. (예: 10:30)")
        LocalTime closeBusinessHours
){

}

