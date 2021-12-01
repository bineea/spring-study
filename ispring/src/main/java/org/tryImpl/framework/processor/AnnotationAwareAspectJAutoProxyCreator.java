package org.tryImpl.framework.processor;

import org.tryImpl.framework.annotation.Aspect;
import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.ConfigurableBeanFactory;
import org.tryImpl.framework.context.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationAwareAspectJAutoProxyCreator extends AbstractAutoProxyCreator {

    private volatile List<String> aspectBeanNames;

    private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    protected List<Advisor> findCandidateAdvisors() {
        List<Advisor> candidateAdvisors = super.findCandidateAdvisors();
        if (candidateAdvisors == null ||  candidateAdvisors.isEmpty()) {
            candidateAdvisors.addAll(this.buildAspectJAdvisors());
        }
        return candidateAdvisors;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName) {
        return new Object[0];
    }

    @Override
    protected Object createProxy(Class<?> clazz, String beanName, Object[] specificInterceptors) {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ConfigurableBeanFactory)) {
            throw new RuntimeException("AnnotationAwareAspectJAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    private List<Advisor> buildAspectJAdvisors() {
        List<String> aspectNames = this.aspectBeanNames;

        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    List<Advisor> advisors = new ArrayList<>();
                    aspectNames = new ArrayList<>();
                    //TODO 解析aspect
                    String[] beanNamesForType = beanFactory.getBeanNamesForType(Object.class);
                    for (String beanName : beanNamesForType) {
                        Class<?> beanType = beanFactory.getType(beanName);
                        if (beanType.isAnnotationPresent(Aspect.class)) {
                            //TODO
                            advisors.add(null);
                        }
                    }
                }
            }
        }

        if (aspectNames.isEmpty()) {
            return Collections.emptyList();
        } else {
            //TODO 映射封装advisor
            return null;
        }
    }
}
