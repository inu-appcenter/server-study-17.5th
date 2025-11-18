package server.Heeyoung.domain.User.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Heeyoung.domain.User.Service.UserAuthService;
import server.Heeyoung.domain.User.dto.request.UserLoginDto;
import server.Heeyoung.domain.User.dto.request.UserSignUpDto;
import server.Heeyoung.global.jwt.JwtTokenResponseDto;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApiSpecification {

    private final UserAuthService userAuthService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUpUser(@Valid @RequestBody UserSignUpDto dto) {
        userAuthService.signUp(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공하였습니다.");
    }

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<JwtTokenResponseDto> login(@RequestBody UserLoginDto dto) {
        JwtTokenResponseDto loginUserToken = userAuthService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(loginUserToken);
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenResponseDto> reissueToken(@RequestHeader("Refresh-Token") String refreshToken) {
        JwtTokenResponseDto newToken = userAuthService.reissueToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(newToken);
    }
}
