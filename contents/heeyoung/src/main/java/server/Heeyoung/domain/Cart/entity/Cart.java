package server.Heeyoung.domain.Cart.entity;

import jakarta.persistence.*;
import lombok.*;
import server.Heeyoung.domain.CartMenu.entity.CartMenu;
import server.Heeyoung.domain.Order.entity.Order;
import server.Heeyoung.domain.Store.entity.Store;
import server.Heeyoung.domain.User.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "store_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartMenu> cartMenuList = new ArrayList<>();

    @Builder
    private Cart(Store store, User user) {
        this.store = store;
        this.user = user;
    }

    public void addCartMenu(CartMenu cartMenu) {
        this.cartMenuList.add(cartMenu);
        cartMenu.setCart(this);
    }
}
