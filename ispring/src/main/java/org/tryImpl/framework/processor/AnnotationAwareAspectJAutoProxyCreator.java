package org.tryImpl.framework.processor;

import org.tryImpl.framework.aop.Advisor;
import org.tryImpl.framework.aop.BeanFactoryAspectJAdvisorsBuilder;
import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.ConfigurableListableBeanFactory;

import java.util.List;

public class AnnotationAwareAspectJAutoProxyCreator extends AbstractAutoProxyCreator {

    private BeanFactoryAspectJAdvisorsBuilder beanFactoryAspectJAdvisorsBuilder;

    @Override
    protected List<Advisor> findCandidateAdvisors() {
        List<Advisor> candidateAdvisors = super.findCandidateAdvisors();
        if (candidateAdvisors == null ||  candidateAdvisors.isEmpty()) {
            candidateAdvisors.addAll(beanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors());
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
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new RuntimeException("AnnotationAwareAspectJAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        beanFactoryAspectJAdvisorsBuilder = new BeanFactoryAspectJAdvisorsBuilder((ConfigurableListableBeanFactory) beanFactory);
    }

}
