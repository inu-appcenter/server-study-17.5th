package server.dongmin.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "store")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String category;

    @Column
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private BigDecimal minDeliveryPrice;

    @Column(nullable = false)
    private BigDecimal deliveryTip;

    @Column(nullable = false)
    private String businessHours;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static Store create(String storeName, String category, String content,
                               String address, BigDecimal minDeliveryPrice, BigDecimal deliveryTip,
                               String businessHours) {
        return Store.builder()
                .storeName(storeName)
                .category(category)
                .content(content)
                .address(address)
                .minDeliveryPrice(minDeliveryPrice)
                .deliveryTip(deliveryTip)
                .businessHours(businessHours)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
