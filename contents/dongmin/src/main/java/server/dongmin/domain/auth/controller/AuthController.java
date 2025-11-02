package server.dongmin.domain.auth.controller;

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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        authService.logout(userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtToken> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        JwtToken newToken = authService.reissue(refreshToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + newToken.getAccessToken())
                .header("Refresh-Token", newToken.getRefreshToken())
                .body(newToken);
    }

}
