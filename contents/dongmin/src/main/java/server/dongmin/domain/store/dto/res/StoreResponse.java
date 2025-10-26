package server.dongmin.domain.store.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResponse{

    private Long storeId;
    private Long userId;
    private String storeName;
    private StoreCategory category;
    private String content;
    private String address;
    private BigDecimal minDeliveryPrice;
    private BigDecimal deliveryTip;
    private LocalTime openBusinessHours;
    private LocalTime closeBusinessHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StoreResponse from(Store store){
        return StoreResponse.builder()
                .storeId(store.getStoreId())
                .userId(store.getUserId())
                .storeName(store.getStoreName())
                .category(store.getCategory())
                .content(store.getContent())
                .address(store.getAddress())
                .minDeliveryPrice(store.getMinDeliveryPrice())
                .deliveryTip(store.getDeliveryTip())
                .openBusinessHours(store.getOpenBusinessHours())
                .closeBusinessHours(store.getCloseBusinessHours())
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getModifiedAt())
                .build();
    }
}
