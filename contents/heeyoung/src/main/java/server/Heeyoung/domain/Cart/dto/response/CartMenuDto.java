package server.Heeyoung.domain.Cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuDto {
    private final String menuName;
    private final Long quantity;
}
