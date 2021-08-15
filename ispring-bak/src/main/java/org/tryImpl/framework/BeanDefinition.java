package org.tryImpl.framework;

public class BeanDefinition {
    private String scope;
    private boolean lazy;
    private Class<?> beanClass;

    public String getScope() {
        return scope;
    }

    public boolean isLazy() {
        return lazy;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
