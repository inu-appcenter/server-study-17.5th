package server.dongmin.domain.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.dongmin.domain.store.dto.req.StoreRequest;
import server.dongmin.domain.store.dto.req.StoreSearchCondition;
import server.dongmin.domain.store.dto.res.StoreResponse;
import server.dongmin.domain.store.service.StoreService;
import server.dongmin.domain.user.entity.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController implements StoreControllerSpecification {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody StoreRequest storeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(userDetails, storeRequestDto));
    }

    @GetMapping
    public ResponseEntity<Slice<StoreResponse>> getStores(
            @ModelAttribute StoreSearchCondition condition,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStore(condition, pageable));
    }
}
