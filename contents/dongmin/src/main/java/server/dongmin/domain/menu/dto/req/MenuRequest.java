package server.dongmin.domain.menu.dto.req;

import java.math.BigDecimal;

public record MenuRequest(
        String menuName,
        BigDecimal price,
        String content,
        String category
) {

}
