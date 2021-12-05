package org.tryImpl.framework.aop;

import org.tryImpl.framework.context.ListableBeanFactory;
import org.tryImpl.framework.intercept.MethodInterceptor;

import java.lang.reflect.Method;

public class AspectJAroundAdvice implements MethodInterceptor, Advice {

    private ListableBeanFactory beanFactory;
    private String beanName;
    private Method aspectJAroundAdviceMethod;

    public AspectJAroundAdvice(ListableBeanFactory beanFactory, String beanName, Method aspectJAroundAdviceMethod) {
        this.beanFactory = beanFactory;
        this.beanName = beanName;
        this.aspectJAroundAdviceMethod = aspectJAroundAdviceMethod;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object aspectBean = this.beanFactory.getBean(this.beanName);
        ProceedingJoinPoint pjp = new MethodInvocationProceedingJoinPoint(methodInvocation);
        return this.aspectJAroundAdviceMethod.invoke(aspectBean, pjp);
    }
}
