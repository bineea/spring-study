package org.tryImpl.framework.aop;

import java.lang.reflect.Method;

public class MethodInvocationProceedingJoinPoint implements ProceedingJoinPoint {

    private MethodInvocation methodInvocation;

    public MethodInvocationProceedingJoinPoint(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodInvocation.proceed();
    }

    @Override
    public Method getMethod() {
        return methodInvocation.getMethod();
    }
}
