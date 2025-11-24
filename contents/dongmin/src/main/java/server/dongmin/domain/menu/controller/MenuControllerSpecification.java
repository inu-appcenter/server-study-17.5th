package server.dongmin.domain.menu.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import server.dongmin.domain.menu.dto.req.MenuRequest;
import server.dongmin.domain.menu.dto.res.MenuResponse;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.global.exception.error.ErrorResponse;

@Tag(name = "MenuController", description = "메뉴 컨트롤러")
public interface MenuControllerSpecification {

    @Operation(summary = "createMenu", description = "메뉴 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "✅ 메뉴 생성 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MenuResponse.class))),
            @ApiResponse(responseCode = "401", description = "❌ 인증되지 않은 사용자",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "❌ 가게 주인이 아님",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "❌ 존재하지 않는 가게",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<MenuResponse> createMenu(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId,
            @Valid @RequestBody MenuRequest menuRequest);



    @Operation(summary = "getMenus", description = "메뉴 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 목록 조회 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Slice.class))),
            @ApiResponse(responseCode = "404", description = "❌ 존재하지 않는 가게",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Slice<MenuResponse>> getMenus(
            @PathVariable Long storeId,
            Pageable pageable);


}
