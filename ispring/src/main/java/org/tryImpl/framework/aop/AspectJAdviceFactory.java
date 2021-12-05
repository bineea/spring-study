package org.tryImpl.framework.aop;

import org.tryImpl.framework.context.ListableBeanFactory;

import java.lang.reflect.Method;

public interface AspectJAdviceFactory {

    Advice getAdvice(AspectJExpressionPointcut aspectJExpressionPointcut, Method method, ListableBeanFactory beanFactory, String beanName);
}
