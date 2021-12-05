package org.tryImpl.framework.aop;

import org.tryImpl.framework.context.ListableBeanFactory;

import java.lang.reflect.Method;

/**
 * Title: InstantiationModelAwarePointcutAdvisorImpl<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2021/12/3 14:35
 */
public class InstantiationModelAwarePointcutAdvisorImpl implements PointcutAdvisor{

    private Pointcut pointcut;
    private Advice advice;
    private AspectJAdviceFactory aspectJAdviceFactory;
    private ListableBeanFactory beanFactory;
    private String beanName;

    public InstantiationModelAwarePointcutAdvisorImpl(AspectJExpressionPointcut aspectJExpressionPointcut, Method method, AspectJAdviceFactory aspectJAdviceFactory, ListableBeanFactory beanFactory, String beanName) {
        this.pointcut = aspectJExpressionPointcut;
        this.aspectJAdviceFactory = aspectJAdviceFactory;
        this.beanFactory = beanFactory;
        this.beanName = beanName;
        this.advice = this.initializationAdvice(aspectJExpressionPointcut, method);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    private Advice initializationAdvice(AspectJExpressionPointcut aspectJExpressionPointcut, Method method) {
        return aspectJAdviceFactory.getAdvice(aspectJExpressionPointcut, method, beanFactory, beanName);
    }
}
