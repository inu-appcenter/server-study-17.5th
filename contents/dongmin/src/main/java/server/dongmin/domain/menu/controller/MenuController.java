package server.dongmin.domain.menu.controller;

import jakarta.validation.Valid;
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
import server.dongmin.domain.user.entity.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}/menus")
public class MenuController implements MenuControllerSpecification {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId,
            @Valid @RequestBody MenuRequest menuRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuService.createMenu(userDetails, storeId, menuRequest));
    }

    @GetMapping
    public ResponseEntity<Slice<MenuResponse>> getMenus(
            @PathVariable Long storeId,
            Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(menuService.getMenusByStoreId(storeId, pageable));
    }

}
