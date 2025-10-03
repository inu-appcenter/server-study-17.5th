package server.dongmin.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;
import java.time.LocalTime;

@Table(name = "store")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

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

    public static Store create(String storeName, StoreCategory category, String content,
                               String address, BigDecimal minDeliveryPrice, BigDecimal deliveryTip,
                               LocalTime openBusinessHours, LocalTime closeBusinessHours) {
        return Store.builder()
                .storeName(storeName)
                .category(category)
                .content(content)
                .address(address)
                .minDeliveryPrice(minDeliveryPrice)
                .deliveryTip(deliveryTip)
                .openBusinessHours(openBusinessHours)
                .closeBusinessHours(closeBusinessHours)
                .build();
    }
}
