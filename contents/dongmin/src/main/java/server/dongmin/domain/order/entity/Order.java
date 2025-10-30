package server.dongmin.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.order.enums.OrderStatus;
import server.dongmin.domain.order.enums.PaymentMethod;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "orders")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column
    private String request;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private LocalDateTime deliveredAt;

    private Order(Long userId, Long storeId, BigDecimal totalPrice, PaymentMethod paymentMethod,
                  String request, OrderStatus status) {
        this.userId = userId;
        this.storeId = storeId;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.request = request;
        this.status = status;
    }

    //  TODO: Order 도메인 작성 시 인자값 변경
    public static Order create(Long userId, Long storeId, BigDecimal totalPrice, PaymentMethod paymentMethod, String request) {
        return new Order(
                userId,
                storeId,
                totalPrice,
                paymentMethod,
                request,
                OrderStatus.PENDING
        );
    }

    public void completeDelivery(LocalDateTime deliveredAt) {
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = deliveredAt;
    }
}
