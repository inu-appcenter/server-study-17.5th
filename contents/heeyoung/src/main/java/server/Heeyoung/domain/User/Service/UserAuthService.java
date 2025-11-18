package server.Heeyoung.domain.User.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.Heeyoung.domain.RefreshToken.entity.RefreshToken;
import server.Heeyoung.domain.RefreshToken.repository.RefreshTokenRepository;
import server.Heeyoung.domain.User.dto.request.UserSignUpDto;
import server.Heeyoung.domain.User.dto.request.UserLoginDto;
import server.Heeyoung.domain.User.entity.User;
import server.Heeyoung.domain.User.repository.UserRepository;
import server.Heeyoung.global.exception.ErrorCode;
import server.Heeyoung.global.exception.RestApiException;
import server.Heeyoung.global.jwt.JwtTokenResponseDto;
import server.Heeyoung.global.jwt.JwtTokenProvider;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private void saveRefreshToken(User user, String refreshToken) {

        log.debug("RefreshToken 저장 시도. loginId={}", user.getLoginId());

        // 기존 토큰 있는지 확인
        boolean exists = refreshTokenRepository.existsByUserLoginId(user.getLoginId());

        // 존재하면 삭제
        if (exists) {
            log.info("기존 RefreshToken 존재 → 삭제. loginId={}", user.getLoginId());
            refreshTokenRepository.deleteByRefreshToken(user.getLoginId());
        }

        // 새 토큰 저장
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(token);

        log.info("RefreshToken 저장 완료. loginId={}", user.getLoginId());
    }

    // 회원가입
    @Transactional
    public void signUp(UserSignUpDto dto) {

        log.info("회원가입 요청 들어옴. loginId={}, email={}", dto.getLoginId(), dto.getEmail());

        // 아이디 중복 검사
        if (userRepository.existsByLoginId(dto.getLoginId())) {
            log.warn("회원가입 실패 → 중복된 ID. loginId={}", dto.getLoginId());
            throw new RestApiException(ErrorCode.DUPLICATED_ID);
        }
        // 이메일 중복 검사
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("회원가입 실패 → 중복된 이메일. email={}", dto.getEmail());
            throw new RestApiException(ErrorCode.DUPLICATED_EMAIL);
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

            log.info("회원가입 성공. loginId={}", dto.getLoginId());
        }
    }

    // 로그인
    @Transactional
    public JwtTokenResponseDto login(UserLoginDto dto) {

        log.info("로그인 시도. loginId={}", dto.getLoginId());

        // 로그인 아이디/패스워드 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());

        // 인증 객체 생성되면서 loadUserByUsername 메서드 실행
        Authentication auth = authenticationManagerBuilder.getObject().authenticate(authToken);

        log.debug("인증 성공. loginId={}", dto.getLoginId());

        // 인증 정보 기반 Jwt 토큰 생성
        JwtTokenResponseDto tokenDto = jwtTokenProvider.generateTokens(auth);

        log.info("토큰 생성 완료. loginId={}", dto.getLoginId());

        // auth 객체에서 user 꺼내기
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetails.getUser();

        // RefreshToken 저장
        saveRefreshToken(user, tokenDto.getRefreshToken());

        log.info("로그인 성공. loginId={}", dto.getLoginId());

        return tokenDto;
    }

    // RefreshToken 을 이용해 AccessToken 재발급
    @Transactional
    public JwtTokenResponseDto reissueToken(String refreshToken) {

        log.info("AccessToken 재발급 요청");

        // DB 에 refreshToken 존재하는지 확인
        RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (tokenEntity == null) {
            // DB 에 없으면 재로그인 요청
            log.warn("재발급 실패 → RefreshToken DB에 없음");
            throw new RestApiException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // 만료 여부 확인
        if (jwtTokenProvider.isTokenExpired(refreshToken)) {
            log.warn("재발급 실패 → RefreshToken 만료됨. 삭제 처리");
            refreshTokenRepository.delete(tokenEntity);
            // 만료 시 재로그인 요청
            throw new RestApiException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // 토큰 유효하면 access token 재발급
        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        String loginId = claims.getSubject();

        log.debug("RefreshToken 유효. loginId={}", loginId);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    log.error("재발급 실패 → USER_NOT_FOUND. loginId={}", loginId);
                    return new RestApiException(ErrorCode.USER_NOT_FOUND);
                });

        // auth 객체 만들기
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        // AccessToken 재발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(auth);
        log.info("AccessToken 재발급 완료. loginId={}", loginId);

        return JwtTokenResponseDto.builder()
                .type("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
