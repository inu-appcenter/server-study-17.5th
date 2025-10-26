package server.dongmin.domain.basket.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.basket.dto.req.BasketItemRequest;
import server.dongmin.global.BaseTimeEntity;

@Table(name = "basket_item")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public static BasketItem create(Long basketId, BasketItemRequest basketItemRequest) {
        return BasketItem.builder()
                .menuId(basketItemRequest.getMenuId())
                .basketId(basketId)
                .quantity(basketItemRequest.getQuantity())
                .build();
    }

}
