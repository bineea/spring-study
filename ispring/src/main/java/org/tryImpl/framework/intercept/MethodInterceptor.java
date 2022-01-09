package org.tryImpl.framework.intercept;

import org.tryImpl.framework.aop.Advice;
import org.tryImpl.framework.aop.MethodInvocation;

public interface MethodInterceptor extends Advice {

    Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
