package org.tryImpl.framework.processor;

import org.tryImpl.framework.aop.*;
import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.BeanFactoryAware;
import org.tryImpl.framework.context.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;

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
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new RuntimeException("AnnotationAwareAspectJAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        initBeanFactory((ConfigurableListableBeanFactory) beanFactory);
    }

    protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelper(beanFactory);
    }

    private boolean shouldSkip(String beanName, Class<?> beanClass) {
        return false;
    }

    protected List<Advisor> findCandidateAdvisors() {
        return advisorRetrievalHelper.findAdvisorBeans();
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {

        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName);
        if(specificInterceptors != null && specificInterceptors.length > 0) {
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, bean);
            return proxy;
        }
        return bean;
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass) ||
                Pointcut.class.isAssignableFrom(beanClass) ||
                Advisor.class.isAssignableFrom(beanClass);
        return retVal;
    }


    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName) {
        List<Advisor> candidateAdvisors = this.findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = new ArrayList<>();
        for (Advisor advisor : candidateAdvisors) {
            if (advisor instanceof PointcutAdvisor) {
                Pointcut pointcut = ((PointcutAdvisor) advisor).getPointcut();
                if (!pointcut.getClassFilter().matches(clazz)) {
                    continue;
                } else {
                    //FIXME 校验所有method，判断是否需要创建代理类
                    //pointcut.getMethodMatcher().matches()
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
