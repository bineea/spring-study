package org.tryImpl.framework.processor;

public class AnnotationAwareAspectJAutoProxyCreator extends AbstractAutoProxyCreator {

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName) {
        return new Object[0];
    }

    @Override
    protected Object createProxy(Class<?> clazz, String beanName, Object[] specificInterceptors) {
        return null;
    }
}
