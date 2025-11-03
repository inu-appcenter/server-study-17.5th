package server.Heeyoung.domain.User.entity;

import jakarta.persistence.*;
import lombok.*;
import server.Heeyoung.domain.Cart.entity.Cart;
import server.Heeyoung.domain.Order.entity.Order;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @Builder
    private User(String loginId, String password, String email, String phoneNum, String address, String nickname, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
        this.nickname = nickname;
        this.name = name;
    }

    public void clearCart() {
        this.cart = null;
    }

    public void setRefreshToken(String token) {
        this.refreshToken = token;
    }
}
