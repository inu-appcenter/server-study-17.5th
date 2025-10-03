package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.user.entity.User;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public static Basket create(User user, Store store) {
        return Basket.builder()
                .user(user)
                .store(store)
                .build();
    }
}
