package server.dongmin.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import server.dongmin.domain.user.entity.User;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.domain.user.service.UserDetailsServiceImpl;
import server.dongmin.global.exception.error.CustomErrorCode;
import server.dongmin.global.exception.error.RestApiException;
import server.dongmin.global.jwt.refresh.RefreshToken;
import server.dongmin.global.jwt.refresh.RefreshTokenRepository;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidMillisecond;
    private final long refreshTokenValidMillisecond;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.accessTokenValidMillisecond}") long accessTokenValidMillisecond,
                            @Value("${jwt.refreshTokenValidMillisecond}") long refreshTokenValidMillisecond,
                            UserDetailsServiceImpl userDetailsService,
                            RefreshTokenRepository refreshTokenRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidMillisecond = accessTokenValidMillisecond;
        this.refreshTokenValidMillisecond = refreshTokenValidMillisecond;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        String accessToken = createAccessToken(user.getEmail(), authorities);
        String refreshToken = createRefreshToken(user.getEmail());

        upsertRefreshToken(user, refreshToken);

        // TokenInfo 객체 생성 및 반환
        return JwtToken.builder()
                .grantType("Bearer") // 토큰 타입
                .accessToken(accessToken) // Access Token
                .refreshToken(refreshToken) // Refresh Token
                .build(); // TokenInfo 객체 생성
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내 Authentication 객체를 생성
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RestApiException(CustomErrorCode.JWT_INVALID);
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new RestApiException(CustomErrorCode.JWT_MALFORMED);
        } catch (ExpiredJwtException e) {
            throw new RestApiException(CustomErrorCode.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new RestApiException(CustomErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new RestApiException(CustomErrorCode.JWT_INVALID);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Access token 생성
    private String createAccessToken(String username, String authorities) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh token 생성
    private String createRefreshToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 이메일로 authentication을 만들고 토큰 생성
    public JwtToken generateTokenByUsername(String username) {
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        return generateToken(authentication);
    }

    // DB에 해당 유저에 대한 Refresh Token이 존재하면 덮어쓰기
    private void upsertRefreshToken(User user, String refreshTokenValue) {
        refreshTokenRepository.findByUserId(user.getUserId())
                .ifPresentOrElse(
                        // 있으면 update
                        rt -> rt.updateToken(refreshTokenValue),
                        // 없으면 insert
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .user(user)
                                        .refreshToken(refreshTokenValue)
                                        .build()
                        )
                );
    }
}
