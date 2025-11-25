package server.Heeyoung.domain.Cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import server.Heeyoung.domain.Cart.dto.response.CartResponseDto;
import server.Heeyoung.domain.CartMenu.dto.request.CartMenuRequestDto;
import server.Heeyoung.domain.CartMenu.dto.response.CartMenuResponseDto;
import server.Heeyoung.domain.User.Service.UserDetailsImpl;
import server.Heeyoung.global.exception.ErrorResponseDto;
import server.Heeyoung.global.exception.RestApiException;

@Tag(name = "Cart", description = "장바구니 관리 API")
public interface CartApiSpecification {

    // 장바구니 조회
    @Operation(
            summary = "장바구니 조회",
            description = "현재 로그인한 사용자의 장바구니 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅장바구니 조회 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌장바구니를 찾을 수 없음 (CART_NOT_FOUND) 또는 사용자를 찾을 수 없음 (USER_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject(
                                            value = "{ \"error\": \"CART_NOT_FOUND\", \"message\": \"장바구니를 찾을 수 없습니다.\" }"
                                    )
                    )
            )
    })
    ResponseEntity<CartResponseDto> getCart(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    // 장바구니 삭제
    @Operation(
            summary = "장바구니 삭제",
            description = "현재 로그인한 사용자의 장바구니 및 모든 장바구니 메뉴를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅장바구니 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌장바구니를 찾을 수 없음 (CART_NOT_FOUND) 또는 사용자를 찾을 수 없음 (USER_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"CART_NOT_FOUND\", \"message\": \"장바구니를 찾을 수 없습니다.\" }"
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteCart(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    // 장바구니 메뉴 추가
    @Operation(
            summary = "장바구니에 메뉴 추가",
            description = "지정된 메뉴를 장바구니에 추가합니다. 장바구니가 없는 경우 자동으로 생성됩니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅메뉴 추가 성공",
                    content = @Content(schema = @Schema(implementation = CartMenuResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌잘못된 요청 (유효성 검증 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{\"error\": \"400\", \"message\": \"잘못된 입력값입니다.\"}"
                                ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌사용자, 가게, 또는 메뉴를 찾을 수 없음 (USER_NOT_FOUND, STORE_NOT_FOUND, MENU_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"USER_NOT_FOUND\", \"message\": \"사용자를 찾을 수 없습니다.\" }"
                            )
                    )
            )
    })
    ResponseEntity<CartMenuResponseDto> addMenuToCart(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "장바구니에 추가할 메뉴 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CartMenuRequestDto.class))
            )
            @Valid @RequestBody CartMenuRequestDto dto
    );

    // 장바구니 메뉴 수량 업데이트
    @Operation(
            summary = "장바구니 메뉴 수량 수정",
            description = "장바구니에 담긴 특정 메뉴의 수량을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅수량 수정 성공",
                    content = @Content(schema = @Schema(implementation = CartMenuResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌잘못된 수량 (INVALID_QUANTITY)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"INVALID_QUANTITY\", \"message\": \"잘못된 수량입니다.\" }"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "❌권한 없음 - 다른 사용자의 장바구니 (UNAUTHORIZED_CART_ACCESS)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"UNAUTHORIZED_CART_ACCESS\", \"message\": \"접근 권한이 없습니다.\" }"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌장바구니 메뉴를 찾을 수 없음 (CART_MENU_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"CART_MENU_NOT_FOUND\", \"message\": \"접근 권한이 없습니다.\" }"))
            )
    })
    ResponseEntity<CartMenuResponseDto> updateCartMenuQuantity(
            @Parameter(description = "수정할 장바구니 메뉴 ID", required = true)
            @PathVariable Long cartMenuId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "새로운 수량 (1 이상)", required = true, example = "3")
            @RequestParam Long newQuantity
    );

    // 장바구니 메뉴 삭제
    @Operation(
            summary = "장바구니 메뉴 삭제",
            description = "장바구니에서 특정 메뉴를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅메뉴 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "❌권한 없음 - 다른 사용자의 장바구니 (UNAUTHORIZED_CART_ACCESS)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"UNAUTHORIZED_CART_ACCESS\", \"message\": \"다른 사용자의 장바구니입니다.\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌장바구니 메뉴를 찾을 수 없음 (CART_MENU_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                                    examples = @ExampleObject(
                                        value = "{ \"error\": \"CART_MENU_NOT_FOUND\", \"message\": \"장바구니에서 해당 메뉴를 찾을 수 없습니다.\" }"
    ))
            )
    })
    ResponseEntity<Void> deleteCartMenu(
            @Parameter(description = "삭제할 장바구니 메뉴 ID", required = true)
            @PathVariable Long cartMenuId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    );

}
