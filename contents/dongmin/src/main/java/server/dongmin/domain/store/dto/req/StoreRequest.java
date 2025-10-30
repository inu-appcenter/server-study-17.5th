package server.dongmin.domain.store.dto.req;

import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

public record StoreRequest(
        String storeName,
        StoreCategory category,
        String content,
        String address,
        BigDecimal minDeliveryPrice,
        BigDecimal deliveryTip,
        LocalTime openBusinessHours,
        LocalTime closeBusinessHours
){

}

