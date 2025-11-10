package server.dongmin.domain.menu.dto.req;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record MenuRequest(

        @NotBlank(message = "메뉴 이름이 비어있습니다.")
        String menuName,

        @NotBlank(message = "가격이 비어있습니다.")
        BigDecimal price,

        String content,

        @NotBlank(message = "카테고리가 비어있습니다.")
        String category
) {

}
