package org.tryImpl.framework.context;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {

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
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> beanNameResult = new ArrayList<>();
        for (String beanName : beanDefinitionNameList) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getBeanClass() != null
                    && (type.isAssignableFrom(beanDefinition.getBeanClass()) || Objects.equals(type, beanDefinition.getBeanClass()))) {
                beanNameResult.add(beanName);
            } else if (RootBeanDefinition.class.isAssignableFrom(beanDefinition.getClass())
                    && (type.isAssignableFrom(((RootBeanDefinition) beanDefinition).getFactoryMethodReturnType()) || Objects.equals(type, ((RootBeanDefinition) beanDefinition).getFactoryMethodReturnType()))) {
                beanNameResult.add(beanName);
            }
        }
        return beanNameResult.toArray(new String[beanNameResult.size()]);
    }

    @Override
    public void preInstantiateSingletons() {
        for (String beanName : beanDefinitionNameList) {
            this.getBean(beanName);
        }
    }
}
