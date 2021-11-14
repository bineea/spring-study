package org.tryImpl.framework.processor;

import org.tryImpl.framework.context.BeanDefinitionRegistry;

public interface BeanDefinitionRegistryPostProcessor extends BeanPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);
}
