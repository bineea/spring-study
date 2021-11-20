package org.tryImpl.framework.context;

import java.util.List;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    List<String> getBeanDefinitionNames();
}
