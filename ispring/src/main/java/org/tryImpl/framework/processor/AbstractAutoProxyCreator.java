package org.tryImpl.framework.processor;

public abstract class AbstractAutoProxyCreator implements BeanPostProcessor{

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        bean.getClass();

        return null;
    }
}
