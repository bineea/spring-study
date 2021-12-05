package org.tryImpl.framework.processor;

import org.tryImpl.framework.aop.Advisor;
import org.tryImpl.framework.aop.JdkDynamicAopProxy;
import org.tryImpl.framework.aop.Pointcut;
import org.tryImpl.framework.aop.PointcutAdvisor;
import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.BeanFactoryAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInstantiation(String beanName, Class<?> beanClass) {
        //默认所有bean对象均需要创建代理对象
        if (shouldSkip(beanName, beanClass)) {
            return null;
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    private boolean shouldSkip(String beanName, Class<?> beanClass) {
        return false;
    }

    protected List<Advisor> findCandidateAdvisors() {
        return Collections.emptyList();
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName);
        if(specificInterceptors != null && specificInterceptors.length > 0) {
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, bean);
            return proxy;
        }
        return bean;
    }

    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName) {
        List<Advisor> advisors = this.findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = new ArrayList<>();
        for (Advisor advisor : advisors) {
            if (advisor instanceof PointcutAdvisor) {
                Pointcut pointcut = ((PointcutAdvisor) advisor).getPointcut();
                if (pointcut.getClassFilter().matches(clazz)) {
                    eligibleAdvisors.addAll(advisors);
                }
            }
        }
        return eligibleAdvisors.isEmpty() ? null : eligibleAdvisors.toArray();
    }

    protected Object createProxy(Class<?> clazz, String beanName, Object[] specificInterceptors, Object targetBeanObj) {
        List<Advisor> advisors = new ArrayList<>();
        for (Object specificInterceptor : specificInterceptors) {
            if (specificInterceptor instanceof Advisor) {
                advisors.add((Advisor) specificInterceptor);
            }
        }
        Object proxyInstance = new JdkDynamicAopProxy(advisors, targetBeanObj).getProxy(clazz.getClassLoader(), clazz.getInterfaces());
        return proxyInstance;
    }
}
