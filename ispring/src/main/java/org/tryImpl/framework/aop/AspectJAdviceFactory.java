package org.tryImpl.framework.aop;

import java.lang.reflect.Method;

public interface AspectJAdviceFactory {

    Advice getAdvice(AspectJExpressionPointcut aspectJExpressionPointcut, Method method);
}
