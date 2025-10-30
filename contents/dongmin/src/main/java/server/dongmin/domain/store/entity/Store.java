package server.dongmin.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.store.dto.req.StoreRequest;
import server.dongmin.domain.store.enums.StoreCategory;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;
import java.time.LocalTime;

@Table(name = "stores")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private BigDecimal minDeliveryPrice;

    @Column(nullable = false)
    private BigDecimal deliveryTip;

    @Column(nullable = false)
    private LocalTime openBusinessHours;

    @Column(nullable = false)
    private LocalTime closeBusinessHours;

    private Store(Long userId, String storeName, StoreCategory category, String content, String address,
                  BigDecimal minDeliveryPrice, BigDecimal deliveryTip, LocalTime openBusinessHours, LocalTime closeBusinessHours){
        this.userId = userId;
        this.storeName = storeName;
        this.category = category;
        this.content = content;
        this.address = address;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.openBusinessHours = openBusinessHours;
        this.closeBusinessHours = closeBusinessHours;
    }

    public static Store create(Long userId, StoreRequest storeRequest) {
        return new Store(
                userId,
                storeRequest.storeName(),
                storeRequest.category(),
                storeRequest.content(),
                storeRequest.address(),
                storeRequest.minDeliveryPrice(),
                storeRequest.deliveryTip(),
                storeRequest.openBusinessHours(),
                storeRequest.closeBusinessHours()
        );
    }
}
