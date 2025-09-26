package server.Heeyoung.domain.CartMenu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(nullable = false, name = "menu_id")
    private Long menuId;

    @Column(nullable = false, name = "cart_id")
    private Long cartId;

    @Builder
    private CartMenu(Long id, Long cartMenuQuantity,  Long menuId, Long cartId) {
        this.id = id;
        this.cartMenuQuantity = cartMenuQuantity;
        this.menuId = menuId;
        this.cartId = cartId;
    }
}
