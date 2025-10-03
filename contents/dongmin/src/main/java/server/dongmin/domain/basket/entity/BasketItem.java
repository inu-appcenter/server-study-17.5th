package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.global.BaseTimeEntity;

@Table(name = "basket_item")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class BasketItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", nullable = false)
    private Basket basket;

    @Column(nullable = false)
    private int quantity;

    public static BasketItem create(Menu menu, Basket basket, int quantity) {
        return BasketItem.builder()
                .menu(menu)
                .basket(basket)
                .quantity(quantity)
                .build();
    }

}
