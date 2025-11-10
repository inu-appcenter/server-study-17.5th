package server.dongmin.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.menu.dto.req.MenuRequest;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;

@Table(name = "menus")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String content;

    @Column(nullable = false)
    private String category;

    private Menu(Long storeId, String menuName, BigDecimal price, String content, String category) {
        this.storeId = storeId;
        this.menuName = menuName;
        this.price = price;
        this.content = content;
        this.category = category;
    }

    public static Menu create(Long storeId, MenuRequest menuRequest) {
        return new Menu(
                storeId,
                menuRequest.menuName(),
                menuRequest.price(),
                menuRequest.content(),
                menuRequest.category()
        );
    }
}
