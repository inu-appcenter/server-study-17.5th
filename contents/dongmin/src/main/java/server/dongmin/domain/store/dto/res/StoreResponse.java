package server.dongmin.domain.store.dto.res;

import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

public record StoreResponse(
        Long storeId,
        Long userId,
        String storeName,
        StoreCategory category,
        String content,
        String address,
        BigDecimal minDeliveryPrice,
        BigDecimal deliveryTip,
        LocalTime openBusinessHours,
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
