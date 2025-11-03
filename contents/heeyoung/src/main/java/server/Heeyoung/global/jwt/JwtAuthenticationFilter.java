package server.Heeyoung.global.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // header 에서 토큰 추출하기 위한 메서드
    private String getTokenFromRequest(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token==null) {
            return null;
        }
        return token.substring(7); // "Bearer " 이후 토큰 부분
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // request header 에서 jwt 토큰 추출
            String token = getTokenFromRequest(request);

            // 토큰 유효성 검사
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰 유효하다면 인층 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 인증 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 다음 필터로 이동
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            throw e;
        }
    }
}
