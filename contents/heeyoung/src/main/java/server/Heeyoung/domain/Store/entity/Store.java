package server.Heeyoung.domain.Store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.Menu.entity.Menu;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String storePhone;

    @Column(nullable = false)
    private Long minPrice;

    @Column(nullable = false)
    private String storeAddress;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    @Builder
    private Store(String storeName, String storePhone, Long minPrice, String storeAddress) {
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.minPrice = minPrice;
        this.storeAddress = storeAddress;
    }

    public void addMenu(Menu menu) {
        this.menuList.add(menu);
        menu.setStore(this);
    }


}
