package server.Heeyoung.domain.User.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.Heeyoung.domain.User.dto.request.UserSignUpDto;
import server.Heeyoung.domain.User.dto.request.UserLoginDto;
import server.Heeyoung.domain.User.entity.User;
import server.Heeyoung.domain.User.repository.UserRepository;
import server.Heeyoung.global.exception.ErrorCode;
import server.Heeyoung.global.exception.RestApiException;
import server.Heeyoung.global.jwt.JwtTokenResponseDto;
import server.Heeyoung.global.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public void signUp(UserSignUpDto dto) {
        // 아이디 중복 검사
        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new RestApiException(ErrorCode.DUPLICATED_ID);
        }
        else {
            User user = User.builder()
                    .loginId(dto.getLoginId())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .phoneNum(dto.getPhoneNum())
                    .name(dto.getName())
                    .nickname(dto.getNickname())
                    .address(dto.getAddress())
                    .build();
            userRepository.save(user);
        }
    }

    // 로그인
    @Transactional
    public JwtTokenResponseDto login(UserLoginDto dto) {

        // 로그인 아이디/패스워드 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());

        // 인증 객체 생성되면서 loadUserByUsername 메서드 실행
        Authentication auth = authenticationManagerBuilder.getObject().authenticate(authToken);

        // 인증 정보 기반 Jwt 토큰 생성
        JwtTokenResponseDto tokenDto = jwtTokenProvider.generateTokens(auth);

        // auth 객체에서 user 꺼내기
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetails.getUser();

        // RefreshToken 저장
        user.setRefreshToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

    // RefreshToken 을 이용해 AccessToken 재발급
    @Transactional
    public JwtTokenResponseDto reissueToken(String refreshToken) {

        try {
            // getClaims 에서 검증 + 추출
            Claims claims = jwtTokenProvider.getClaims(refreshToken);
            String loginId = claims.getSubject();

            // DB 조회 및 토큰 일치 확인
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND));

            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new RestApiException(ErrorCode.INVALID_TOKEN);
            }

            // auth 객체 만들기
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            // AccessToken 재발급
            String newAccessToken = jwtTokenProvider.generateAccessToken(auth);

            return JwtTokenResponseDto.builder()
                    .type("Bearer")
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (JwtException e) {
            throw new RestApiException(ErrorCode.INVALID_TOKEN);
        }
    }
}
