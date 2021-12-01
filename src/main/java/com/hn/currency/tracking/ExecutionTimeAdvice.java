package com.hn.currency.tracking;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled:true}")
public class ExecutionTimeAdvice {

    @Around("@annotation(com.hn.currency.tracking.TrackDuration)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object object = point.proceed();
        stopWatch.stop();

        LOGGER.debug("{}.{}: Duration {}ms",
                    point.getSignature().getDeclaringTypeName(),
                    point.getSignature().getName(),
                    stopWatch.getLastTaskTimeMillis());
        return object;
    }
}