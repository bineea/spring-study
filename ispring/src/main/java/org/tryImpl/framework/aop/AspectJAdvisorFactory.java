package org.tryImpl.framework.aop;

import org.tryImpl.framework.context.ListableBeanFactory;

import java.util.List;

public interface AspectJAdvisorFactory {

    List<Advisor> getAdvisors(Class<?> clazz, ListableBeanFactory beanFactory, String beanName);
}
