package server.Heeyoung.domain.Menu.Dto.RequestDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuCreateRequestDto {

    private final String menuName;

    private final Long price;

    private final String menuPicture;
}
