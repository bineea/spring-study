package org.tryImpl.framework.context;

public class RootBeanDefinition extends BeanDefinition {

    private Class<?> factoryMethodReturnType;

    public Class<?> getFactoryMethodReturnType() {
        return factoryMethodReturnType;
    }

    public void setFactoryMethodReturnType(Class<?> factoryMethodReturnType) {
        this.factoryMethodReturnType = factoryMethodReturnType;
    }
}
