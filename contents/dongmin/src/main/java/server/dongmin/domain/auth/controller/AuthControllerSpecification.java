package server.dongmin.domain.auth.controller;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import server.dongmin.domain.auth.dto.req.LoginRequest;
import server.dongmin.domain.auth.dto.req.SignUpRequest;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.global.exception.error.ErrorResponse;
import server.dongmin.global.jwt.JwtToken;

@Tag(name = "AuthController", description = "로그인 및 회원가입 컨트롤러")
public interface AuthControllerSpecification {
    @Operation(summary = "signup", description = "회원가입 시 사용되는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "✅ 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "❌ 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "❌ 중복된 리소스",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "이메일 중복", value = "{\"error\": \"409\", \"message\": \"이미 가입된 이메일 입니다.\"}"),
                                    @ExampleObject(name = "닉네임 중복", value = "{\"error\": \"409\", \"message\": \"이미 존재하는 별명 입니다.\"}"),
                                    @ExampleObject(name = "전화번호 중복", value = "{\"error\": \"409\", \"message\": \"이미 가입된 번호 입니다.\"}")
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest);



    @Operation(summary = "login", description = "로그인 시 사용되는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 로그인 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JwtToken.class))),
            @ApiResponse(responseCode = "400", description = "❌ 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "❌ 인증 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<JwtToken> login(@Valid @RequestBody LoginRequest loginRequest);



    @Operation(summary = "logout", description = "로그아웃 시 사용되는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "✅ 로그아웃 성공"),
            @ApiResponse(responseCode = "404", description = "❌ 존재하지 않는 리프레시 토큰",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails);



    @Operation(summary = "reissue", description = "토큰 재발급 시 사용되는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 토큰 재발급 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JwtToken.class))),
            @ApiResponse(responseCode = "401", description = "❌ 유효하지 않은 토큰",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 토큰", value = "{\"error\": \"401\", \"message\": \"유효하지 않은 토큰입니다.\"}"),
                                    @ExampleObject(name = "만료된 토큰", value = "{\"error\": \"401\", \"message\": \"만료된 엑세스 토큰입니다.\"}"),
                                    @ExampleObject(name = "잘못된 토큰 형식", value = "{\"error\": \"401\", \"message\": \"잘못된 토큰 형식입니다.\"}"),
                                    @ExampleObject(name = "유효하지 않은 서명", value = "{\"error\": \"401\", \"message\": \"유효하지 않은 서명입니다.\"}"),
                                    @ExampleObject(name = "지원하지 않는 토큰", value = "{\"error\": \"401\", \"message\": \"지원하지 않는 토큰입니다.\"}"),
                                    @ExampleObject(name = "사용자 불일치", value = "{\"error\": \"401\", \"message\": \"토큰의 사용자가 일치하지 않습니다.\"}")
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "❌ 존재하지 않는 리프레시 토큰",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<JwtToken> reissue(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestHeader("Refresh-Token") String refreshToken);


}
