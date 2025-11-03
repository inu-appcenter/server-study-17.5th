package server.Heeyoung.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import server.Heeyoung.domain.User.Service.UserDetailsImpl;
import server.Heeyoung.domain.User.Service.UserDetailsServiceImpl;
import server.Heeyoung.domain.User.entity.User;
import server.Heeyoung.domain.User.repository.UserRepository;
import server.Heeyoung.global.exception.ErrorCode;
import server.Heeyoung.global.exception.RestApiException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key; // JWT 서명을 만들고 검증하는 비밀키
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiredTime;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiredTime;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, UserRepository userRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userRepository = userRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody(); // 페이로드(Claims) 리턴
    }

    // AccessToken 생성
    public String generateAccessToken(Authentication authentication) {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        // accessToken 발급
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth",authorities)
                .setExpiration(new Date((now.getTime() + accessTokenExpiredTime)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 생성
    public String generateRefreshToken(Authentication authentication) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiredTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 최초 로그인 시 두 개 동시에 발급
    public JwtTokenResponseDto generateTokens(Authentication authentication) {
        return JwtTokenResponseDto.builder()
                .type("Bearer")
                .accessToken(generateAccessToken(authentication))
                .refreshToken(generateRefreshToken(authentication))
                .build();
    }

    // jwt 토큰을 복호화해 토큰에 들어있는 정보를 꺼내 Authentication 객체를 생성
    public Authentication getAuthentication(String token) {

        // jwt 토큰에서 사용자 정보 추출
        Claims claims = getClaims(token);

        // 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        String loginId = claims.getSubject();

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND));

        // UserDetails 객체 생성
        UserDetailsImpl userDetails = userDetailsServiceImpl.loadUserByUsername(user.getLoginId());

        // Authentication 리턴
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {

        if (token == null)
            return false;

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰의 형식이 올바르지 않습니다.");
        } catch (SignatureException | SecurityException e) {
            throw new JwtException("토큰의 서명이 올바르지 않습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("토큰의 형식이 만료되었습니다.");
        }
    }
}
