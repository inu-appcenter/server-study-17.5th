package server.Heeyoung.domain.Order.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.OrderDetail.entity.OrderDetail;
import server.Heeyoung.domain.Store.entity.Store;
import server.Heeyoung.domain.User.entity.User;

import java.util.List;

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

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "store_id")
    private Store store;

    @Builder
    private Order(Long id, String paymentMethod, Long totalPrice, String request,  User user, Store store) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.request = request;
        this.user = user;
        this.store = store;
    }
}
