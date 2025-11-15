package server.dongmin.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import server.dongmin.global.exception.error.CustomErrorCode;
import server.dongmin.global.exception.error.ErrorResponse;
import server.dongmin.global.exception.error.RestApiException;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        try{
            filterChain.doFilter(request, response);
        }catch (RestApiException e){
            log.error("JwtExceptionFilter: JWT Exception occurred - {}", e.getMessage());
            setErrorResponse(response, (CustomErrorCode) e.getErrorCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, CustomErrorCode errorCode) throws IOException{
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        ErrorResponse errorResponse = ErrorResponse.create(errorCode.getCode(), errorCode.getMessage());

        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
}
