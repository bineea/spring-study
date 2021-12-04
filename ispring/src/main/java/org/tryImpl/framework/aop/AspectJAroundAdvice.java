package org.tryImpl.framework.aop;

import org.tryImpl.framework.intercept.MethodInterceptor;

import java.lang.reflect.Method;

public class AspectJAroundAdvice implements MethodInterceptor, Advice {



    @Override
    public Object invoke(Method method, Object[] args) throws Throwable {
        return null;
    }
}
