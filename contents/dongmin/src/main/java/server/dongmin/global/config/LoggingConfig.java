package server.dongmin.global.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LoggingConfig {

    /**
     * Pointcut: server.dongmin.domain 패키지 하위의 모든 controller 패키지에 있는 클래스들을 대상으로 함
     */
    @Pointcut("within(server.dongmin.domain..*.controller..*)")
    public void controller() {}

    /**
     * 컨트롤러의 메서드가 실행되기 전, 후 그리고 예외 발생 시 로그를 기록
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // HttpServletRequest 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 로그 메시지 포맷팅
        log.info("======> API REQUEST [START] <======");
        log.info("=> REQUEST URI: [{}] {}", request.getMethod(), request.getRequestURI());
        log.info("=> CONTROLLER: {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

        // 파라미터 로깅 (민감한 정보가 포함될 수 있으므로 주의)
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.info("=> PARAMETERS: {}", Arrays.toString(args));
        } else {
            log.info("=> PARAMETERS: None");
        }

        Object result = null;
        try {
            // 실제 타겟 메소드 실행
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            log.error("=> EXCEPTION: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("=> RETURN VALUE: {}", result);
            log.info("=> EXECUTION TIME: {}ms", executionTime);
            log.info("======> API REQUEST [END] <======");
        }
    }
}
