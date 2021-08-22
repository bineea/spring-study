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
    protected void createSingletonBean() {
        for(String beanName : beanNameList) {
            Object obj = getSingleton(beanName);
            if(obj == null) {

            }


            //创建代理
        }
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
