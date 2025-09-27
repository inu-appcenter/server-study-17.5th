package server.Heeyoung.domain.CartMenu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.Cart.entity.Cart;
import server.Heeyoung.domain.Menu.entity.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartmenu_id")
    private Long id;

    @Column(nullable = false, name = "cartmenu_quantity")
    private Long cartMenuQuantity;

    // FK
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "cart_id")
    private Cart cart;

    @Builder
    private CartMenu(Long id, Long cartMenuQuantity, Menu menu, Cart cart) {
        this.id = id;
        this.cartMenuQuantity = cartMenuQuantity;
        this.menu = menu;
        this.cart = cart;
    }
}
