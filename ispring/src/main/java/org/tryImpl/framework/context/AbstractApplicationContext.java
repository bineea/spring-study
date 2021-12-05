package org.tryImpl.framework.context;

import org.tryImpl.framework.processor.BeanDefinitionRegistryPostProcessor;
import org.tryImpl.framework.processor.BeanPostProcessor;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractApplicationContext implements BeanFactory {

    protected Object keepSafeOperation = new Object();

    protected Set<String> singletonsCurrentlyInCreation = new HashSet<>();

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Class<?> getType(String beanName) {
        return getBeanFactory().getType(beanName);
    }

    protected final void refresh() {
        synchronized (this.keepSafeOperation) {
            ConfigurableListableBeanFactory beanFactory = refreshBeanFactory();
            invokeBeanFactoryPostProcessor(beanFactory);
            registerBeanPostProcessor(beanFactory);
            createSingletonBean(beanFactory);
        }
    }

    /**
     * 更新beanFactory
     */
    protected ConfigurableListableBeanFactory refreshBeanFactory() {
        return getBeanFactory();
    }

    /**
     * 执行beanFactoryPostProcessor
     */
    protected void invokeBeanFactoryPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
        for (String beanName : beanNames) {
            BeanDefinitionRegistryPostProcessor bean = (BeanDefinitionRegistryPostProcessor) beanFactory.getBean(beanName);
            bean.postProcessBeanDefinitionRegistry((BeanDefinitionRegistry) getBeanFactory());
        }
    }

    /**
     * 解析并注册beanPostProcessor
     */
    protected void registerBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class);
        for (String beanName : beanNames) {
            BeanPostProcessor beanPostProcessor = (BeanPostProcessor) beanFactory.getBean(beanName);
            beanFactory.addBeanPostPorcessor(beanPostProcessor);
        }
    }

    /**
     * 创建单例bean
     */
    protected void createSingletonBean(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

}
