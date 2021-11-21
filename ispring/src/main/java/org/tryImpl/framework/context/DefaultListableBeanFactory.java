package org.tryImpl.framework.context;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    protected Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    protected List<String> beanDefinitionNameList = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        BeanDefinition existingDefinition = this.getBeanDefinition(beanName);
        if (existingDefinition != null) {
            beanDefinitionMap.put(beanName, beanDefinition);
        } else {
            beanDefinitionMap.put(beanName, beanDefinition);
            beanDefinitionNameList.add(beanName);
        }
    }

    @Override
    public List<String> getBeanDefinitionNames() {
        return beanDefinitionNameList;
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }
}
