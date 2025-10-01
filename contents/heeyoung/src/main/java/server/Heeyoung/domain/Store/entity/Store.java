package server.Heeyoung.domain.Store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, name = "store_name")
    private String storeName;

    @Column(nullable = false, name = "store_phone")
    private String storePhone;

    @Column(nullable = false, name = "min_price")
    private Long minPrice;

    @Column(nullable = false, name = "store_address")
    private String storeAddress;

    @Builder
    private Store(String storeName, String storePhone, Long minPrice, String storeAddress) {
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.minPrice = minPrice;
        this.storeAddress = storeAddress;
    }


}
