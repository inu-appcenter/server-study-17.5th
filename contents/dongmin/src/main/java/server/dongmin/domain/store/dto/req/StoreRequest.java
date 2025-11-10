package server.dongmin.domain.store.dto.req;

import jakarta.validation.constraints.NotBlank;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

public record StoreRequest(

        @NotBlank(message = "가게 이름이 비어있습니다.")
        String storeName,

        @NotBlank(message = "카테고리가 비어있습니다.")
        StoreCategory category,

        String content,

        @NotBlank(message = "주소가 비어있습니다.")
        String address,

        @NotBlank(message = "최소 배달 금액이 비어있습니다.")
        BigDecimal minDeliveryPrice,

        @NotBlank(message = "배달 팁이 비어있습니다.")
        BigDecimal deliveryTip,

        @NotBlank(message = "오픈 시간이 비어있습니다.")
        LocalTime openBusinessHours,

        @NotBlank(message = "마감 시간이 비어있습니다.")
        LocalTime closeBusinessHours
){

}

