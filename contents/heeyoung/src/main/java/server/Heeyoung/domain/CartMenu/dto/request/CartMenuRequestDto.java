package server.Heeyoung.domain.CartMenu.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartMenuRequestDto {

    private final Long menuId;

    private final Long cartMenuQuantity;

}
