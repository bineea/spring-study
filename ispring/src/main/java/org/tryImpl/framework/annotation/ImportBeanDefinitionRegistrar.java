package org.tryImpl.framework.annotation;

import org.tryImpl.framework.context.BeanDefinitionRegistry;

public interface ImportBeanDefinitionRegistrar {

    void registerBeanDefinitions(BeanDefinitionRegistry beanDefinitionRegistry);
}
