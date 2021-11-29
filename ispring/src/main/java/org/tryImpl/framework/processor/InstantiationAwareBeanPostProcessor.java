package org.tryImpl.framework.processor;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 目标bean实例化前，返回目标bean可能要使用的代理
     * @param beanName
     * @param beanClass
     * @return
     */
    Object postProcessBeforeInstantiation(String beanName, Class<?> beanClass);
}
