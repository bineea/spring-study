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
        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName);
        if(specificInterceptors != null) {
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors);
            return proxy;
        }
        return bean;
    }

    protected abstract Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName);

    protected abstract Object createProxy(Class<?> clazz, String beanName, Object[] specificInterceptors);
}
