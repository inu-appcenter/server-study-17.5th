package server.dongmin.domain.basket.dto.req;

import lombok.Getter;

@Getter
public class BasketItemRequest {

    private Long menuId;
    private int quantity;

}
