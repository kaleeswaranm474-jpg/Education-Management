package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.service..*(..))")
    public Object logServiceExecution(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long startTime = System.currentTimeMillis();

        logger.info(
                "Started method: {}",
                joinPoint.getSignature().getName()
        );

        try {

            Object result = joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis() - startTime;

            logger.info(
                    "Completed method: {} in {} ms",
                    joinPoint.getSignature().getName(),
                    executionTime
            );

            return result;

        } catch (Exception exception) {

            logger.error(
                    "Error in method: {} - {}",
                    joinPoint.getSignature().getName(),
                    exception.getMessage()
            );

            throw exception;
        }
    }
}