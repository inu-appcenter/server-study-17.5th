package server.Heeyoung.domain.Menu.Dto.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.Heeyoung.domain.Menu.entity.Menu;
import server.Heeyoung.domain.Store.entity.Store;

@Getter
@Builder
public class MenuResponseDto {

    private final Long id;

    private final String menuName;

    private final Long price;

    private final String menuPicture;

    private final Long storeId;

    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuPicture(menu.getMenuPicture())
                .storeId(menu.getStore().getId())
                .build();
    }

}
