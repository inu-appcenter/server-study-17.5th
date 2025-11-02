package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.basket.dto.req.BasketItemRequest;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;

@Table(name = "basket_items")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasketItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketItemId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "basket_id", nullable = false)
    private Long basketId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    private BasketItem(Long menuId, Long basketId, int quantity, BigDecimal price) {
        this.menuId = menuId;
        this.basketId = basketId;
        this.quantity = quantity;
        this.price =  price;
    }

    public static BasketItem create(Long basketId, BasketItemRequest basketItemRequest,BigDecimal price) {
        return new BasketItem(
                basketItemRequest.menuId(),
                basketId,
                basketItemRequest.quantity(),
                price
        );
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

}
