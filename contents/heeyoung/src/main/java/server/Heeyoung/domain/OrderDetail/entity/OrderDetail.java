package server.Heeyoung.domain.OrderDetail.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(nullable = false, name = "menu_id")
    private Long menuId;

    @Column(nullable = false, name = "order_id")
    private Long orderId;

    @Builder
    private OrderDetail(Long id, String  orderDetailQuantity, Long orderDetailPrice, Long menuId, Long storeId) {
        this.id = id;
        this.orderDetailQuantity = orderDetailQuantity;
        this.orderDetailPrice = orderDetailPrice;
        this.menuId = menuId;
        this.orderId = storeId;
    }
}
