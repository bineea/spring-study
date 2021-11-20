package org.tryImpl.framework.context;

import java.util.List;

public class DefaultListableBeanFactory extends AbstractBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        //TODO
    }

    @Override
    public List<String> getBeanDefinitionNames() {
        //TODO
        return null;
    }

}
