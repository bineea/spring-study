package org.tryImpl.framework;

import org.tryImpl.framework.annotation.*;
import org.tryImpl.framework.processor.BeanPostProcessor;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class AnnotationConfigApplicationContext {

    private HashMap<String, BeanDefinition> beanDefinitionHashMap = new HashMap<>();
    private HashMap<String, Object> singletonBeanHashMap =new HashMap<>();
    private HashSet<BeanPostProcessor> beanPostProcessorHashSet = new HashSet<>();

    public AnnotationConfigApplicationContext(Class<?>... configClasses) {

        //扫描class文件
        //获取class信息，根据class信息组装BeanDefinition
        this.register(configClasses);
        //根据BeanDefinition创建非懒加载单例Bean
        //获取需创建Bean的所有字段，并完成字段注入
        this.instanceSingletonBean();
    }

    //通过ComponentScan注解，扫描所有class文件
    //实例化BeanDefinition
    //判断是否实现‘后置处理器’接口，完成后置处理器的实例化及缓存操作！因为创建Bean的时候，需要根据后置处理器对Bean执行操作！
    //根据class信息设置BeanDefinition

    //问题1：后置处理器的实例化操作有问题！因为创建Bean的时候，仍会创建后置处理器的Bean，就会出现后置处理器存在多个java实例
    //问题2：后置处理器的实例化操作并没有实现属性字段的注入
    public void register(Class<?>... configClasses) {

        Set<Class<?>> beanClassSet = getBeanClass(configClasses);
        for(Class<?> beanClass : beanClassSet) {
            if(beanClass.isAnnotationPresent(Component.class)) {
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setBeanClass(beanClass);

                //处理后置处理器（存在问题！！！）
                //Class.isAssignableFrom方法判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。如果是则返回 true；否则返回 false。如果该 Class 表示一个基本类型，且指定的 Class 参数正是该 Class 对象，则该方法返回 true；否则返回 false。
                if(BeanPostProcessor.class.isAssignableFrom(beanClass)) {
                    try {
                        BeanPostProcessor beanPostProcessor = (BeanPostProcessor) beanClass.getDeclaredConstructor().newInstance();
                        beanPostProcessorHashSet.add(beanPostProcessor);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }

                //处理BeanDefinition
                if(beanClass.isAnnotationPresent(Scope.class)) {
                    String scopeValue = beanClass.getAnnotation(Scope.class).value();
                    if(ScopeEnum.prototype.name().equals(scopeValue)) {
                        beanDefinition.setScope(ScopeEnum.prototype.name());
                    } else {
                        beanDefinition.setScope(ScopeEnum.singleton.name());
                    }
                }
                if(beanClass.isAnnotationPresent(Lazy.class)) {
                    beanDefinition.setLazy(beanClass.getAnnotation(Lazy.class).value());
                }
                String beanName = beanClass.getAnnotation(Component.class).value();
                if(beanName == null || beanName.isEmpty()) {
                    beanName = beanClass.getName().toLowerCase();
                    beanName = beanName.substring(beanName.lastIndexOf('.')+1);
                }
                beanDefinitionHashMap.put(beanName, beanDefinition);
            }
        }
    }

    private Set<Class<?>> getBeanClass(Class<?>[] configClasses) {
        Set<Class<?>> beanClassSet = new HashSet<>();
        for(Class<?> configClass : configClasses) {
            if(!configClass.isAnnotationPresent(Configuration.class)) {
                continue;
            }
            if(configClass.isAnnotationPresent(ComponentScan.class)) {
                String scanPath = configClass.getAnnotation(ComponentScan.class).value();
                scanPath = scanPath.replace(".", "/");
                URL classUrl = ClassLoader.getSystemClassLoader().getResource(scanPath);
                File classUrlFile = new File(classUrl.getFile());
                scanFileDirectory2Class(classUrlFile, beanClassSet);
            } else {
                beanClassSet.add(configClass);
            }
        }
        return beanClassSet;
    }

    private void scanFileDirectory2Class(File classUrlFile, Set<Class<?>> beanClassSet) {
        if(classUrlFile.isDirectory()) {
            for(File classFile : classUrlFile.listFiles()) {
                String classFilePath = classFile.getAbsolutePath();
                if(classFile.isFile() && classFilePath.endsWith(".class")) {
                    try {
                        String fileName = classFilePath.substring(classFilePath.lastIndexOf("org"), classFilePath.lastIndexOf(".class"));
                        fileName = fileName.replace(File.separatorChar, '.');
                        Class<?> beanClass = ClassLoader.getSystemClassLoader().loadClass(fileName);
                        beanClassSet.add(beanClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if(classFile.isDirectory()) {
                    scanFileDirectory2Class(classFile, beanClassSet);
                }
            }
        }
    }

    //根据BeanDefinition进行实例化操作
    //通过BeanDefinition中的class信息获取所有字段信息
    //判断字段是否存在Autowired注解，通过getBean方法获取字段对应Bean实例，并完成赋值操作
    //缓存所有单例Bean

    //问题1：没有实现byType功能，字段对应Bean实例是通过BeanName获取的
    //问题2：没有解决循环依赖问题
    //问题3：后置处理器可能存在问题，没有指定循环顺序的操作，可能存在操作被覆盖
    public void instanceSingletonBean() {
        for(String beanName : beanDefinitionHashMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionHashMap.get(beanName);
            if(ScopeEnum.singleton.name().equals(beanDefinition.getScope())
                && !beanDefinition.isLazy()) {
                Object bean = doCreateBeanInstance(beanName, beanDefinition);
                singletonBeanHashMap.put(beanName, bean);
            }
        }
    }

    private Object doCreateBeanInstance(String beanName, BeanDefinition beanDefinition) {
        try {
            Object bean = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
            for(Field field : beanDefinition.getBeanClass().getDeclaredFields()) {
                if(field.isAnnotationPresent(Autowired.class)) {
                    //此处只实现了byName的效果！！！
                    //此处未解决循环依赖问题！！！
                    String fieldName = field.getName();
                    Object fieldBean = getBean(fieldName);
                    field.setAccessible(true);
                    field.set(bean, fieldBean);
                }
            }

            //BeanNameAware
            if(bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }

            //InitializingBean
            if(bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }

            //BeanPostProcessor
            for(BeanPostProcessor postProcessor : beanPostProcessorHashSet) {
                postProcessor.postProcessAfterInitialization(bean, beanName);
            }

            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object getBean(String beanName) {

        BeanDefinition beanDefinition = beanDefinitionHashMap.get(beanName);
        if(ScopeEnum.singleton.name().equals(beanDefinition.getScope())) {
            Object bean = singletonBeanHashMap.get(beanName);
            if(bean == null) {
                bean = doCreateBeanInstance(beanName, beanDefinition);
                singletonBeanHashMap.put(beanName, bean);
            }
            return bean;
        } else {
            return doCreateBeanInstance(beanName, beanDefinition);
        }
    }

}
