package server.dongmin.domain.store.dto.req;

import lombok.*;
import server.dongmin.domain.store.enums.StoreCategory;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
public class StoreRequest{

    private String storeName;
    private StoreCategory category;
    private String content;
    private String address;
    private BigDecimal minDeliveryPrice;
    private BigDecimal deliveryTip;
    private LocalTime openBusinessHours;
    private LocalTime closeBusinessHours;

}

