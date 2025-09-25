package server.Heeyoung.domain.Cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // FK
    @Column(nullable = false, name = "store_id")
    private Long storeId;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Builder
    private Cart(Long id, Long storeId, Long userId) {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
    }
}
