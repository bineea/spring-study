package org.tryImpl.framework.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
