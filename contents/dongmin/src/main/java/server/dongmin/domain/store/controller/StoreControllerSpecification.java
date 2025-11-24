package server.dongmin.domain.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import server.dongmin.domain.store.dto.req.StoreRequest;
import server.dongmin.domain.store.dto.req.StoreSearchCondition;
import server.dongmin.domain.store.dto.res.StoreResponse;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.global.exception.error.ErrorResponse;

@Tag(name = "StoreController", description = "가게 컨트롤러")
public interface StoreControllerSpecification {

    @Operation(summary = "createStore", description = "가게 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "✅ 가게 생성 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StoreResponse.class))),
            @ApiResponse(responseCode = "401", description = "❌ 인증되지 않은 사용자",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "❌ 가게를 만들 수 없는 유저",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<StoreResponse> createStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody StoreRequest storeRequestDto);

    @Operation(summary = "getStores", description = "가게 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 목록 조회 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Slice.class)))
    })
    ResponseEntity<Slice<StoreResponse>> getStores(
            @ModelAttribute StoreSearchCondition condition,
            Pageable pageable);
}
