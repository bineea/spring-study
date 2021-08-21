package org.tryImpl.framework.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractApplicationContext implements BeanFactory {

    protected Map<String, Object> singletonObjects = new HashMap<>();

    protected Map<String, Object> singletonFactories = new HashMap<>();

    protected Map<String, Object> earlySingletonObjects = new HashMap<>();

    protected Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    protected List<String> beanNameList = new ArrayList<>();

    protected Object keepSafeOperation = new Object();

    protected final void create() {
        synchronized (this.keepSafeOperation) {
            registerBeanDefinition();
            createSingletonBean();
        }
    }

    /**
     * 解析并注册beanDefinition
     */
    protected abstract void registerBeanDefinition();

    /**
     * 创建单例bean
     */
    protected abstract void createSingletonBean();

    @Override
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }
}
