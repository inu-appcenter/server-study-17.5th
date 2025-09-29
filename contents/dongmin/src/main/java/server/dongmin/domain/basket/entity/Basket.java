package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.domain.order.entity.Order;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private int quantity;

    public static Basket create(User user, Menu menu, int quantity) {
        return Basket.builder()
                .user(user)
                .menu(menu)
                .quantity(quantity)
                .build();
    }

    public void completeOrder(Order order) {
        this.order = order;
    }
}
