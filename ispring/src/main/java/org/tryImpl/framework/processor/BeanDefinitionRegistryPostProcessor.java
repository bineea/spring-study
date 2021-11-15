package org.tryImpl.framework.processor;

import org.tryImpl.framework.context.BeanDefinitionRegistry;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor{

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);
}
