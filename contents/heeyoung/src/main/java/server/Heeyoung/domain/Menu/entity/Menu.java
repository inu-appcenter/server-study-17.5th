package server.Heeyoung.domain.Menu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;
import server.Heeyoung.domain.Store.entity.Store;

import java.awt.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Long price;

    @Column
    private String menuPicture;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "store_id")
    private Store store;

    @Builder
    private Menu(String menuName, Long price, String menuPicture, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.menuPicture = menuPicture;
        this.store = store;
    }
}
