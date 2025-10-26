package server.dongmin.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.store.dto.req.StoreRequest;
import server.dongmin.domain.store.enums.StoreCategory;
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

    public static Store create(Long userId, StoreRequest storeRequest) {
        return Store.builder()
                .userId(userId)
                .storeName(storeRequest.getStoreName())
                .category(storeRequest.getCategory())
                .content(storeRequest.getContent())
                .address(storeRequest.getAddress())
                .minDeliveryPrice(storeRequest.getMinDeliveryPrice())
                .deliveryTip(storeRequest.getDeliveryTip())
                .openBusinessHours(storeRequest.getOpenBusinessHours())
                .closeBusinessHours(storeRequest.getCloseBusinessHours())
                .build();
    }
}
