package server.dongmin.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;

@Table(name = "order_items")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    private OrderItem(Long orderId, Long menuId, int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    //  TODO: Order 도메인 작성 시 인자값 변경
    public static OrderItem create(Long orderId, Long menuId, int quantity, BigDecimal price) {
        return new OrderItem(
                orderId,
                menuId,
                quantity,
                price
        );
    }
}
