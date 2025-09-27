package server.dongmin.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.store.entity.Store;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "menu")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String content;

    @Column
    private String category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public static Menu create(Store store, String menuName, BigDecimal price, String content, String category) {
        return Menu.builder()
                .store(store)
                .menuName(menuName)
                .price(price)
                .content(content)
                .category(category)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
