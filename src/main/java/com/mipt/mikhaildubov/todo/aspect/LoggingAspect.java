package com.mipt.mikhaildubov.todo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging method execution in the service layer.
 */
@Aspect
@Component
public class LoggingAspect {
  @Around("execution(* com.mipt.mikhaildubov.todo.service.*.*(..))")
  public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("--> Service method start: " + joinPoint.getSignature().getName() +
        " | Args: " + Arrays.toString(joinPoint.getArgs()));

    Object result = joinPoint.proceed();

    System.out.println("<-- Service method end: " + joinPoint.getSignature().getName() +
        " | Result: " + (result != null ? result : "void"));
    return result;
  }
}