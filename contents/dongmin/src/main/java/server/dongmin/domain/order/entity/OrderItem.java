package server.dongmin.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;

@Table(name = "orders_item")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    public static OrderItem create(Order order, Menu menu, int quantity, BigDecimal price) {
        return OrderItem.builder()
                .order(order)
                .menu(menu)
                .quantity(quantity)
                .price(price)
                .build();
    }
}
