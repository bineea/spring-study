package com.example.IspringTest.aspect;

import org.tryImpl.framework.annotation.Around;
import org.tryImpl.framework.annotation.Aspect;
import org.tryImpl.framework.annotation.Component;
import org.tryImpl.framework.aop.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
public class LogAspect {

    @Around(value = "execution(com.example.IspringTest.service.HelloworldService.sayHelloworld())")
    public Object aroundExcute(ProceedingJoinPoint proceedingJoinPoint) {
        Method method = proceedingJoinPoint.getMethod();
        LocalDateTime now = LocalDateTime.now();
        System.out.println("--->>>aroud执行方法before："+method.getName()+"，当前时间为："+now);
        Object proceed = null;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("--->>>aroud执行方法after："+method.getName()+"，当前时间为："+end);
        return proceed;
    }
}
