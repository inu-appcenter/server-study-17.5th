package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.global.BaseTimeEntity;

@Table(name = "basket")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Basket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    public void updateStore(Long storeId) {
        this.storeId = storeId;
    }

    public static Basket create(Long userId, Long storeId) {
        return Basket.builder()
                .userId(userId)
                .storeId(storeId)
                .build();
    }
}
