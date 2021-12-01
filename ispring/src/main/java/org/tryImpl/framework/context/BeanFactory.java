package org.tryImpl.framework.context;

public interface BeanFactory {

    Object getBean(String beanName);

    Class<?> getType(String beanName);
}
