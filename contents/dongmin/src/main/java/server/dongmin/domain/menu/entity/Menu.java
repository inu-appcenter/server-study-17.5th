package server.dongmin.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.menu.dto.req.MenuRequest;
import server.dongmin.global.BaseTimeEntity;

import java.math.BigDecimal;

@Table(name = "menu")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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

    @Column
    private String category;

    public static Menu create(Long storeId, MenuRequest menuRequest) {
        return Menu.builder()
                .storeId(storeId)
                .menuName(menuRequest.getMenuName())
                .price(menuRequest.getPrice())
                .content(menuRequest.getContent())
                .category(menuRequest.getCategory())
                .build();
    }
}
