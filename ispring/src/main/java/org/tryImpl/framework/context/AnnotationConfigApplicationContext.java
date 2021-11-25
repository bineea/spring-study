package org.tryImpl.framework.context;

import org.tryImpl.framework.annotation.*;
import org.tryImpl.framework.processor.BeanDefinitionRegistryPostProcessor;
import org.tryImpl.framework.processor.ConfigurationClassPostProcessor;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final DefaultListableBeanFactory beanFactory;

    private static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME = "internalConfigurationAnnotationProcessor";

    private Set<Class<?>> classSet;

    private AnnotationConfigApplicationContext() {

        this.beanFactory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME);
        beanDefinition.setScope(ScopeEnum.singleton);
        beanDefinition.setLazy(false);
        beanDefinition.setBeanClass(ConfigurationClassPostProcessor.class);

        this.registerBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, beanDefinition);
    }

    public AnnotationConfigApplicationContext(Class<?>... classes) {
        this();
        this.init(classes);
        super.refresh();
    }

    private void init(Class<?>... classes) {
        if(classes == null || classes.length <= 0) {
            throw new IllegalArgumentException("ispring配置类无效-至少配置一个ispring配置类");
        }
        classSet = new HashSet<Class<?>>();
        for(Class<?> clazz : classes) {
            if(!clazz.isAnnotationPresent(Configuration.class)) {
                continue;
            }
            classSet.add(clazz);
        }
        if(classSet.size() <= 0) {
            throw new IllegalArgumentException("ispring配置类无效-ispring配置类必须设置@Configuration注解");
        }
    }

    @Override
    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    //FIXME 通过参数传递beanFactory暂时无法使用
    @Override
    protected void invokeBeanFactoryPostProcessor(BeanFactory beanFactory) {
        String[] beanNames = getBeanFactory().getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
        for (String beanName : beanNames) {
            BeanDefinitionRegistryPostProcessor bean = (BeanDefinitionRegistryPostProcessor) getBeanFactory().getBean(beanName);
            bean.postProcessBeanDefinitionRegistry(getBeanFactory());
        }
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public List<String> getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

//    private Class<?> scanBeanMethod2Class(Method method) {
//        Method[] declaredMethods = clazz.getDeclaredMethods();
//        for(Method method : declaredMethods) {
//            if(method.isAnnotationPresent(Bean.class)) {
//                beanClassSet.add(this.scanBeanMethod2Class(method));
//            }
//        }
//        return null;
//    }
}
