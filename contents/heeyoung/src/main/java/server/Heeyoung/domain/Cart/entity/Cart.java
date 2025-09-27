package server.Heeyoung.domain.Cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.Store.entity.Store;
import server.Heeyoung.domain.User.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // FK
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Builder
    private Cart(Long id, Store store, User user) {
        this.id = id;
        this.store = store;
        this.user = user;
    }
}
