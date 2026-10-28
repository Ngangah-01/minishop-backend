package io.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAspect.class);

    private static final String SERVICE_METHODS =
            "execution(* com.minishop.backend..*Service.*(..))";

    @Before(SERVICE_METHODS)
    public void logBefore(JoinPoint joinPoint) {

        log.debug(
                "Calling {}.{}() with arguments: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs()
        );
    }

    @AfterReturning(
            pointcut = SERVICE_METHODS,
            returning = "result"
    )
    public void logAfterReturning(
            JoinPoint joinPoint,
            Object result
    ) {

        log.debug(
                "Completed {}.{}() with result: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result
        );
    }

    @AfterThrowing(
            pointcut = SERVICE_METHODS,
            throwing = "exception"
    )
    public void logAfterThrowing(
            JoinPoint joinPoint,
            Throwable exception
    ) {

        log.error(
                "Exception in {}.{}(): {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage(),
                exception
        );
    }

    @Around(SERVICE_METHODS)
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();

        } finally {

            long duration =
                    System.currentTimeMillis() - start;

            log.debug(
                    "{}.{}() executed in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    duration
            );
        }
    }
}