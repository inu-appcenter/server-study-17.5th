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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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

    public static Order create(Long userId, Long storeId, BigDecimal totalPrice,
                               PaymentMethod paymentMethod, String request) {
        return Order.builder()
                .userId(userId)
                .storeId(storeId)
                .totalPrice(totalPrice)
                .paymentMethod(paymentMethod)
                .request(request)
                .status(OrderStatus.Pending)
                .build();
    }

    public void completeDelivery(LocalDateTime deliveredAt) {
        this.status = OrderStatus.Delivered;
        this.deliveredAt = deliveredAt;
    }
}
