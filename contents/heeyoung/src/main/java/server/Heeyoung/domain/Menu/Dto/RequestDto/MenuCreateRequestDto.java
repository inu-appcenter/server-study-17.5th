package server.Heeyoung.domain.Menu.Dto.RequestDto;

import lombok.Getter;

@Getter
public class MenuCreateRequestDto {

    private String menuName;

    private Long price;

    private String menuPicture;
}
