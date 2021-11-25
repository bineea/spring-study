package org.tryImpl.framework.context;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractApplicationContext implements BeanFactory {

    protected Object keepSafeOperation = new Object();

    protected Set<String> singletonsCurrentlyInCreation = new HashSet<>();

    public abstract ListableBeanFactory getBeanFactory();

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    protected final void refresh() {
        synchronized (this.keepSafeOperation) {
            BeanFactory beanFactory = refreshBeanFactory();
            invokeBeanFactoryPostProcessor(beanFactory);
            registerBeanPostProcessor(beanFactory);
            createSingletonBean(beanFactory);
        }
    }

    /**
     * 更新beanFactory
     */
    protected BeanFactory refreshBeanFactory() {
        return getBeanFactory();
    }

    /**
     * 执行beanFactoryPostProcessor
     */
    protected abstract void invokeBeanFactoryPostProcessor(BeanFactory beanFactory);

    /**
     * 解析并注册beanPostProcessor
     */
    protected void registerBeanPostProcessor(BeanFactory beanFactory) {

    }

    /**
     * 创建单例bean
     */
    protected void createSingletonBean(BeanFactory beanFactory) {

    }

}
