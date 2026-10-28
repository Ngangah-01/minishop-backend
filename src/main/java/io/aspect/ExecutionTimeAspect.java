package io.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(io.aspect.LogExecutionTime)")
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable{
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end =System.currentTimeMillis();

        System.out.println(
                joinPoint.getSignature().getName()
                + " took " + (end - start) + " ms "
        );
        return result;
    }
}
