package org.tryImpl.framework.context;

public abstract class AbstractBeanFactory implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    private Object doGetBean(String beanName) {
        //TODO
        return null;
    }
}
