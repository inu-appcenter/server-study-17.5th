package server.dongmin.domain.basket.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import server.dongmin.domain.basket.dto.req.BasketItemRequest;
import server.dongmin.domain.basket.dto.res.BasketItemResponse;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.global.exception.error.ErrorResponse;

@Tag(name = "BasketController", description = "장바구니 컨트롤러")
public interface BasketControllerSpecification {

    @Operation(summary = "addBasketItem", description = "장바구니에 아이템 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "✅ 아이템 추가 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BasketItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "❌ 다른 가게 상품 추가",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "❌ 인증되지 않은 사용자",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "❌ 존재하지 않는 메뉴",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<BasketItemResponse> addBasketItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BasketItemRequest basketItemRequest);



    @Operation(summary = "getBasketItems", description = "장바구니 아이템 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 목록 조회 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Slice.class))),
            @ApiResponse(responseCode = "401", description = "❌ 인증되지 않은 사용자",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Slice<BasketItemResponse>> getBasketItems(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable);


}
