package server.Heeyoung.domain.Menu.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import server.Heeyoung.domain.Menu.Dto.RequestDto.MenuCreateRequestDto;
import server.Heeyoung.domain.Menu.Dto.ResponseDto.MenuResponseDto;
import server.Heeyoung.global.exception.ErrorResponseDto;
import server.Heeyoung.global.exception.RestApiException;

@Tag(name = "Menu", description = "메뉴 관리 API")
public interface MenuApiSpecification {

    @Operation(
            summary = "메뉴 등록",
            description = "특정 가게에 새로운 메뉴를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅메뉴 등록 성공",
                    content = @Content(schema = @Schema(implementation = MenuResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌잘못된 요청",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"error\": \"400\", \"message\": \"잘못된 입력값입니다.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌가게를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{\"error\": \"STORE_NOT_FOUND\", \"message\": \"가게를 찾을 수 없습니다.\"}"
                            )
                    )
            )

    })
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long storeId,
            @RequestBody MenuCreateRequestDto dto
    );

}
