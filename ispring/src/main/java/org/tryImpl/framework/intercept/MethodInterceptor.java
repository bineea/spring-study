package org.tryImpl.framework.intercept;

import org.tryImpl.framework.aop.MethodInvocation;

public interface MethodInterceptor {

    Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
