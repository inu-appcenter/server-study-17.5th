package server.dongmin.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.dongmin.domain.auth.service.AuthService;
import server.dongmin.domain.auth.dto.req.LoginRequest;
import server.dongmin.domain.auth.dto.req.SignUpRequest;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.global.jwt.JwtToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerSpecification {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody LoginRequest loginRequest){
        // refresh token을 Cookie에 넣어서 보내기
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        authService.logout(userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtToken> reissue(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestHeader("Refresh-Token") String refreshToken) {
        JwtToken newToken = authService.reissue(userDetails, refreshToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + newToken.getAccessToken())
                .header("Refresh-Token", newToken.getRefreshToken())
                .body(newToken);
    }

}
