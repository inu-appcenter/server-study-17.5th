package server.dongmin.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.dongmin.domain.menu.dto.req.MenuRequest;
import server.dongmin.domain.menu.dto.res.MenuResponse;
import server.dongmin.domain.menu.service.MenuService;
import server.dongmin.domain.store.dto.req.StoreRequest;
import server.dongmin.domain.store.dto.res.StoreResponse;
import server.dongmin.domain.user.entity.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store/{storeId}/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId,
            @RequestBody MenuRequest menuRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuService.createMenu(userDetails, storeId, menuRequest));
    }

    @GetMapping
    public ResponseEntity<Slice<MenuResponse>> getStores(
            @PathVariable Long storeId,
            Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenusByStoreId(storeId, pageable));
    }

}
