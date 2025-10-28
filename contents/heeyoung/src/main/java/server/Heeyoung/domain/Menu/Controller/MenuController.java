package server.Heeyoung.domain.Menu.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Heeyoung.domain.Menu.Dto.RequestDto.MenuCreateRequestDto;
import server.Heeyoung.domain.Menu.Dto.ResponseDto.MenuResponseDto;
import server.Heeyoung.domain.Menu.Service.MenuService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDto dto
    ) {
        MenuResponseDto response = menuService.createMenu(dto, storeId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
