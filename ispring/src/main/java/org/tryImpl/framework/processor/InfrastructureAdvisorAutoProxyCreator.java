package org.tryImpl.framework.processor;

public class InfrastructureAdvisorAutoProxyCreator extends AbstractAutoProxyCreator {

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName) {
        return new Object[0];
    }
}
