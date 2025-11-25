package server.Heeyoung.domain.User.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import server.Heeyoung.domain.User.dto.request.UserLoginDto;
import server.Heeyoung.domain.User.dto.request.UserSignUpDto;
import server.Heeyoung.global.jwt.JwtTokenResponseDto;

@Tag(name = "User", description = "유저 관련 API")
public interface UserApiSpecification {

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "✅회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "❌회원가입 실패 - 잘못된 입력값",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"error\": \"400\", \"message\": \"이메일 형식이 올바르지 않습니다.\"}"
                            ))),
            @ApiResponse(responseCode = "409", description = "❌중복된 아이디 또는 이메일",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"error\": \"409\", \"message\": \"이미 사용 중인 아이디 또는 이메일입니다.\"}"
                            )))
    })
    public ResponseEntity<String> signUpUser(@Valid @RequestBody UserSignUpDto dto);

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "✅로그인 성공",
                    content = @Content(schema = @Schema(implementation = JwtTokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "❌잘못된 입력값",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples =
                                    @ExampleObject(
                                            value = "{\"error\": \"400\", \"message\": \"아이디, 비밀번호는 필수 입력 입니다.\"}"
                                    )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "❌로그인 실패 - 아이디 또는 비밀번호 불일치",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"error\": \"401\", \"message\": \"아이디 또는 비밀번호가 일치하지 않습니다.\"}"
                            )))
    })
    ResponseEntity<JwtTokenResponseDto> login(@RequestBody UserLoginDto dto);

    @Operation(summary = "토큰 재발급", description = "Access Token 을 재발급 받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "✅토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = JwtTokenResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "❌토큰 재발급 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "만료되거나 유효하지 않은 Refresh Token",
                                            value = "{\"error\": \"401\", \"message\": \"Refresh Token이 만료되었습니다. 다시 로그인해주세요.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "존재하지 않는 Refresh Token",
                                            value = "{\"error\": \"401\", \"message\": \"유효하지 않은 Refresh Token입니다.\"}"
                                    )
                            })),
            @ApiResponse(responseCode = "404", description = "❌사용자를 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = "{\"error\": \"404\", \"message\": \"사용자를 찾을 수 없습니다.\"}"
                            )))
    })
    public ResponseEntity<JwtTokenResponseDto> reissueToken(@RequestHeader("Refresh-Token") String refreshToken);
}
