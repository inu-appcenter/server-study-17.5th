package server.dongmin.domain.menu.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MenuRequest(

        @NotNull(message = "메뉴 이름이 비어있습니다.")
        String menuName,

        @NotNull(message = "가격이 비어있습니다.")
        BigDecimal price,

        String content,

        @NotBlank(message = "카테고리가 비어있습니다.")
        String category
) {

}
