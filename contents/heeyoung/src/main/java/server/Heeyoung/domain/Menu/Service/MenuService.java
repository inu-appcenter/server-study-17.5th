package server.Heeyoung.domain.Menu.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.Heeyoung.domain.Menu.Dto.RequestDto.MenuCreateRequestDto;
import server.Heeyoung.domain.Menu.Dto.ResponseDto.MenuResponseDto;
import server.Heeyoung.domain.Menu.entity.Menu;
import server.Heeyoung.domain.Menu.repository.MenuRepository;
import server.Heeyoung.domain.Store.entity.Store;
import server.Heeyoung.domain.Store.repository.StoreRepository;
import server.Heeyoung.global.exception.ErrorCode;
import server.Heeyoung.global.exception.RestApiException;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 메뉴 등록
    @Transactional
    public MenuResponseDto createMenu(MenuCreateRequestDto dto, Long storeId) {
        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()->new RestApiException(ErrorCode.STORE_NOT_FOUND));

        // 메뉴 객체 생성
        Menu menu = Menu.builder()
                .menuName(dto.getMenuName())
                .price(dto.getPrice())
                .menuPicture(dto.getMenuPicture())
                .build();

        // store 의 addMenu 메서드로 메뉴 추가
        store.addMenu(menu);

        // 저장
        menuRepository.save(menu);

        // 응답 dto 에 담아서 반환
        return MenuResponseDto.from(menu);
    }


}
