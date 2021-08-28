package org.tryImpl.framework.intercept;

import java.lang.reflect.Method;

public interface MethodInterceptor {

    Object invoke(Method method, Object[] args) throws Throwable;
}
