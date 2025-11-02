package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.global.BaseTimeEntity;

@Table(name = "baskets")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Basket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    private Basket(Long userId, Long storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }

    public static Basket create(Long userId, Long storeId) {
        return new Basket(
                userId,
                storeId
        );
    }
}
