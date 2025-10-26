package server.dongmin.domain.menu.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import server.dongmin.domain.menu.dto.req.MenuRequest;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.domain.user.entity.UserDetailsImpl;

import java.math.BigDecimal;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

    private Long menuId;
    private Long storeId;
    private String menuName;
    private BigDecimal price;
    private String content;
    private String category;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .menuId(menu.getMenuId())
                .storeId(menu.getStoreId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .content(menu.getContent())
                .category(menu.getCategory())
                .build();
    }

}
