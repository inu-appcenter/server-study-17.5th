package server.Heeyoung.domain.Menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import java.awt.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false, name = "menu_name")
    private String menuName;

    @Column(nullable = false)
    private Long price;

    @Column(name = "menu_picture")
    private String menuPicture;

    // FK
    @Column(nullable = false, name = "store_id")
    private Long storeId;

    @Builder
    private Menu(Long id, String menuName, Long price, String menuPicture, Long storeId) {
        this.id = id;
        this.menuName = menuName;
        this.price = price;
        this.menuPicture = menuPicture;
        this.storeId = storeId;
    }
}
