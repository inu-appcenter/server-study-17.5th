package server.Heeyoung.domain.OrderDetail.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.Menu.entity.Menu;
import server.Heeyoung.domain.Order.entity.Order;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetail_id")
    private Long id;

    @Column(nullable = false, name = "orderdetail_quantity")
    private String orderDetailQuantity;

    @Column(nullable = false, name = "orderDetail_price")
    private Long orderDetailPrice;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    private Order order;

    @Builder
    private OrderDetail(Long id, String  orderDetailQuantity, Long orderDetailPrice, Menu menu, Order order) {
        this.id = id;
        this.orderDetailQuantity = orderDetailQuantity;
        this.orderDetailPrice = orderDetailPrice;
        this.menu = menu;
        this.order = order;
    }
}
