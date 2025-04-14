package ru.kstn.taskmanagementsystem.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* ru.kstn.taskmanagementsystem.services..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("Executing method: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* ru.kstn.taskmanagementsystem.services..*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} returned: {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "execution(* ru.kstn.taskmanagementsystem.services..*.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in method {}: {}",
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
