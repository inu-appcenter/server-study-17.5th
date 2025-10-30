package server.dongmin.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dongmin.domain.menu.dto.req.MenuRequest;
import server.dongmin.domain.menu.dto.res.MenuResponse;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.domain.menu.repository.MenuRepository;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.store.repository.StoreRepository;
import server.dongmin.domain.user.entity.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public MenuResponse createMenu(UserDetailsImpl userDetails, Long storeId, MenuRequest menuRequest) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found")); // TODO: Custom Error

        if (!store.getUserId().equals((userDetails.getUser().getUserId()))){
            throw new IllegalArgumentException("User does not have permission to create a menu"); // TODO: Custom Error
        }

        Menu menu = Menu.create(storeId, menuRequest);
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    @Transactional(readOnly = true)
    public Slice<MenuResponse> getMenusByStoreId(Long storeId, Pageable pageable) {

        if(!storeRepository.existsById(storeId)){
            throw new IllegalArgumentException("Store not found"); // TODO: Custom Error
        }
        return menuRepository.findByStoreId(storeId, pageable).map(MenuResponse::from);
    }
}
