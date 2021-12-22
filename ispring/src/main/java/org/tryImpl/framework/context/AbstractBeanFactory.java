package org.tryImpl.framework.context;

import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.processor.BeanPostProcessor;
import org.tryImpl.framework.processor.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractBeanFactory implements BeanFactory {

    private final Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    private final Map<String, Object> singletonFactories = new HashMap<>();

    private final List<BeanPostProcessor> beanPostProcessorList = new CopyOnWriteArrayList();

    @Override
    public Class<?> getType(String beanName) {
        Object singletonBean = this.getSingletonBean(beanName);
        if (singletonBean != null) {
            return singletonBean.getClass();
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition != null) {
            return beanDefinition.getBeanClass();
        }
        return null;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected List<BeanPostProcessor> getBeanPostProcessorList() {
        return beanPostProcessorList;
    }

    public void addBeanPostPorcessor(BeanPostProcessor beanPostProcessor) {
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
            //尝试通过BeanPostProcessors获取代理对象
            bean = this.resolveBeforeInstantiation(beanName, beanDefinition);
            if (bean != null) {
                return bean;
            }
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

    private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        //尝试通过BeanPostProcessors获取代理对象
        List<BeanPostProcessor> beanPostProcessorList = getBeanPostProcessorList();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            if (InstantiationAwareBeanPostProcessor.class.isAssignableFrom(beanPostProcessor.getClass())) {
                Object instantiation = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanName, beanDefinition.getBeanClass());
                if (instantiation != null) {
                    return instantiation;
                }
            }
        }
        return null;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        try {
            if (beanDefinition.getFactoryMethodName() != null && beanDefinition.getFactoryMethodName().trim().length() > 0) {
                //TODO 执行方法创建bean对象
            }
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
        invokeAwareMethods(beanInstance);
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

    private void invokeAwareMethods(Object beanInstance) {
        if (beanInstance instanceof BeanFactoryAware) {
            ((BeanFactoryAware) beanInstance).setBeanFactory(this);
        }
    }

    /**
     *
     * @param paramTypes
     * @param paramNames
     * @return
     */
    private Object[] createArgumentArray(Class<?>[] paramTypes, String[] paramNames) {
        //TODO 根据class类型和参数名称获取
        return null;
    }

    private Object resolveAutowiredArgument() {
        //TODO 获取方法参数信息
        return null;
    }
}
