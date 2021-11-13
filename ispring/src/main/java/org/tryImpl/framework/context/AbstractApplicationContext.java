package org.tryImpl.framework.context;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AbstractApplicationContext implements BeanFactory {

    protected Map<String, Object> singletonObjects = new HashMap<>();

    protected Map<String, Object> earlySingletonObjects = new HashMap<>();

    protected Map<String, Object> singletonFactories = new HashMap<>();

    protected Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    protected List<String> beanNameList = new ArrayList<>();

    protected Object keepSafeOperation = new Object();

    protected Set<String> singletonsCurrentlyInCreation = new HashSet<>();

    @Override
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected final void create() {
        synchronized (this.keepSafeOperation) {
            registerBeanDefinition();
            registerBeanPostProcessor();
            createSingletonBean();
        }
    }

    /**
     * 解析并注册beanDefinition
     */
    protected abstract void registerBeanDefinition();

    /**
     * 解析并注册beanPostProcessor
     */
    protected abstract void registerBeanPostProcessor();

    /**
     * 创建单例bean
     */
    protected void createSingletonBean() {
        for(String beanName : beanNameList) {
            doCreateSingletonBean(beanName);
        }
    }

    private void doCreateSingletonBean(String beanName) {
        Object obj = getSingleton(beanName);
        if(obj == null) {

        }

        //填充属性

        //后置处理
        //优先简单实现吧，通过beanClass判断是否存在@Transational注解标注的方法，如果存在则进行动态代理
        //1.执行before
        //2.执行init
        //3.执行after
    }

    private Object getSingleton(String beanName) {
        Object obj = singletonObjects.get(beanName);
        if(obj == null) {
            obj = earlySingletonObjects.get(beanName);
            if(obj == null) {
                obj = singletonFactories.get(beanName);
            }
        }
        return obj;
    }

    private Object doCreateBeanInstance(String beanName) {
        try {
            Object sampleInstance = beanDefinitionMap.get(beanName).getBeanClass().getDeclaredConstructor().newInstance();
            return sampleInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
