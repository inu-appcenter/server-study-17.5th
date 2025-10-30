package server.dongmin.domain.menu.dto.res;

import server.dongmin.domain.menu.entity.Menu;

import java.math.BigDecimal;

public record MenuResponse(
        Long menuId,
        Long storeId,
        String menuName,
        BigDecimal price,
        String content,
        String category
) {
    public static MenuResponse from(Menu menu) {
        return new  MenuResponse(
                menu.getMenuId(),
                menu.getStoreId(),
                menu.getMenuName(),
                menu.getPrice(),
                menu.getContent(),
                menu.getCategory()
        );
    }
}
