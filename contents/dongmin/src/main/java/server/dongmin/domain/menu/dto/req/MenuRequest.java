package server.dongmin.domain.menu.dto.req;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MenuRequest {

    private String menuName;
    private BigDecimal price;
    private String content;
    private String category;

}
