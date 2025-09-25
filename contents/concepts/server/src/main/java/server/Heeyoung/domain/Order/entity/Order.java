package server.Heeyoung.domain.Order.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false, name = "payment_method")
    // enum???
    private String paymentMethod;

    @Column(nullable = false, name = "total_price")
    private Long totalPrice;

    private String request;

    // FK
    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "store_id")
    private Long storeId;

    @Builder
    private Order(Long id, String paymentMethod, Long totalPrice, String request, Long userId, Long storeId) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.request = request;
        this.userId = userId;
        this.storeId = storeId;
    }
}
