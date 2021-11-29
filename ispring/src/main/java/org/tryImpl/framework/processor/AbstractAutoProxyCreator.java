package org.tryImpl.framework.processor;

public abstract class AbstractAutoProxyCreator implements InstantiationAwareBeanPostProcessor{

    @Override
    public Object postProcessBeforeInstantiation(String beanName, Class<?> beanClass) {
        //解析切点及方法
        //返回代理对象
        //FIXME 待完善处理逻辑
        shouldSkip(beanName, beanClass);
        if (false) {
            Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName);
            Object proxy = createProxy(beanClass, beanName, specificInterceptors);
            return proxy;
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

    private boolean shouldSkip(String beanName, Class<?> beanClass) {
        //TODO 待完善处理逻辑
        return false;
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
