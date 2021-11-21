package org.tryImpl.framework.context;

import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.processor.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractBeanFactory implements BeanFactory {

    private final Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    private final Map<String, Object> singletonFactories = new HashMap<>();

    private final List<BeanPostProcessor> beanPostProcessorList = new CopyOnWriteArrayList();

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected List<BeanPostProcessor> getBeanPostProcessorList() {
        return beanPostProcessorList;
    }

    protected void addBeanPostPorcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessorList.remove(beanPostProcessor);
        this.beanPostProcessorList.add(beanPostProcessor);
    }

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    private Object doGetBean(String beanName) {
        //获取缓存singletonBean对象，如果已经存在，直接返回
        Object bean = this.getSingletonBean(beanName);
        if (bean != null) {
            return bean;
        } else {
            //获取beanDefinition
            BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
            //反射创建bean实例
            Object beanInstance = this.createBeanInstance(beanDefinition);
            //缓存bean实例
            singletonFactories.put(beanName, beanInstance);
            //赋值bean实例属性
            this.populateBean(beanName, beanDefinition, beanInstance);
            //初始化bean，执行beanPostProcessor
            bean = this.initializeBean(beanName, beanDefinition, beanInstance);
            //缓存singletonBean对象
            this.addSingletonBean(beanName, bean);
        }
        return bean;
    }

    private Object getSingletonBean(String beanName) {
        Object beanObj = this.singletonObjects.get(beanName);
        if (beanObj == null) {
            beanObj = this.earlySingletonObjects.get(beanName);
            if (beanObj == null) {
                synchronized (singletonObjects) {
                    beanObj = this.singletonObjects.get(beanName);
                    if (beanObj == null) {
                        beanObj = this.earlySingletonObjects.get(beanName);
                        if (beanObj == null) {
                            beanObj = this.singletonFactories.get(beanName);
                            if (beanObj != null) {
                                earlySingletonObjects.put(beanName, beanObj);
                                singletonFactories.remove(beanName);
                            }
                        }
                    }
                }
            }
        }
        return beanObj;
    }

    private void addSingletonBean(String beanName, Object singletonBeanObject) {
        synchronized (singletonBeanObject) {
            singletonObjects.put(beanName, singletonBeanObject);
            earlySingletonObjects.remove(beanName);
            singletonFactories.remove(beanName);
        }
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        try {
            Object beanInstance = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
            return beanInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, Object beanInstance) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(Autowired.class) != null) {
                field.setAccessible(true);
                try {
                    Object bean = getBean(field.getName());
                    field.set(beanInstance, bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Object initializeBean(String beanName, BeanDefinition beanDefinition, Object beanInstance) {
        Object result = beanInstance;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessorList()) {
            Object currentBean = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (currentBean == null) {
                currentBean = beanDefinition;
            }
            result = beanPostProcessor.postProcessAfterInitialization(currentBean, beanName);
        }
        return result;
    }

}
