package server.dongmin.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dongmin.domain.auth.dto.req.LoginRequest;
import server.dongmin.domain.auth.dto.req.SignUpRequest;
import server.dongmin.domain.user.entity.User;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.domain.user.repository.UserRepository;
import server.dongmin.global.exception.error.CustomErrorCode;
import server.dongmin.global.exception.error.RestApiException;
import server.dongmin.global.jwt.JwtToken;
import server.dongmin.global.jwt.JwtTokenProvider;
import server.dongmin.global.jwt.refresh.RefreshToken;
import server.dongmin.global.jwt.refresh.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.email())) {
            throw new RestApiException(CustomErrorCode.EMAIL_CONFLICT);
        } else if (userRepository.existsByNickName(signUpRequest.nickName())) {
            throw new RestApiException(CustomErrorCode.NICKNAME_CONFLICT);
        } else if (userRepository.existsByPhoneNumber(signUpRequest.phoneNumber())) {
            throw new RestApiException(CustomErrorCode.NUMBER_CONFLICT);
        }

        User user = User.create(signUpRequest, bCryptPasswordEncoder.encode(signUpRequest.password()));
        userRepository.save(user);
    }

    @Transactional
    public JwtToken login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public void logout(UserDetailsImpl userDetails) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userDetails.getUser().getUserId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.JWT_REFRESH_NOT_FOUND));

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public JwtToken reissue(UserDetailsImpl userDetails, String refreshToken) {
        // 1) JWT 로서 유효한지
        jwtTokenProvider.validateToken(refreshToken); // 유효하지 않으면 여기서 예외

        // 2) DB에 있는지
        RefreshToken saved = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.JWT_REFRESH_NOT_FOUND));

        // 3) 해당 유저의 Refresh Token이 맞는지
        if (!saved.getUserId().equals(userDetails.getUser().getUserId())) {
             throw new RestApiException(CustomErrorCode.JWT_USER_MISMATCH);
        }
        // 4) 유저 정보로 새 토큰 생성
        return jwtTokenProvider.generateTokenByUsername(userDetails.getUser().getEmail());
    }
}
