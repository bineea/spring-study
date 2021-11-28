package org.tryImpl.framework.processor;

import org.tryImpl.framework.annotation.*;
import org.tryImpl.framework.context.AnnotationConfigApplicationContext;
import org.tryImpl.framework.context.BeanDefinition;
import org.tryImpl.framework.context.BeanDefinitionRegistry;
import org.tryImpl.framework.context.ScopeEnum;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 解析配置类，注册beanDefinition
     * @param registry
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        Class<? extends BeanDefinitionRegistry> registryClass = registry.getClass();

        for (String beanName : registry.getBeanDefinitionNames()) {
            this.parse(registry, registry.getBeanDefinition(beanName).getBeanClass());
        }
    }

    private void parse(BeanDefinitionRegistry registry, Class<?> clazz) {

        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            String[] scanPaths = componentScan.value();
            Set<Class<?>> beanClasses = this.scanBeanClass(scanPaths);
            for (Class<?> beanClass : beanClasses) {
                if (beanClass.isAnnotationPresent(Configuration.class)) {
                    this.parse(registry, beanClass);
                } else if (beanClass.isAnnotationPresent(Component.class)){
                    String beanName = beanClass.getAnnotation(Component.class).value();
                    if (beanName == null || beanName.length() <= 0) {
                        int index = beanClass.getName().lastIndexOf(".");
                        beanName = beanClass.getName().substring(index+1,index+2).toLowerCase() + beanClass.getName().substring(index+2);
                    }
                    String scope = beanClass.getAnnotation(Scope.class).scope();
                    boolean lazy = beanClass.getAnnotation(Lazy.class).lazy();
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setBeanName(beanName);
                    beanDefinition.setScope(ScopeEnum.prototype.name().equals(scope) ? ScopeEnum.prototype : ScopeEnum.singleton);
                    beanDefinition.setLazy(lazy);
                    beanDefinition.setBeanClass(beanClass);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }

        //TODO 解析import

//        Method[] methods = clazz.getDeclaredMethods();
//        for (Method method : methods) {
//            if (method.isAnnotationPresent(Bean.class)) {
//                try {
//                    Object methodBeanObject = method.invoke(clazz.getDeclaredConstructor().newInstance(), null);
//                    Class<?> beanClass = method.getReturnType();
//                    String beanName = beanClass.getAnnotation(Bean.class).name();
//                    if (beanName == null || beanName.length() <= 0) {
//                        int index = beanClass.getName().lastIndexOf(".");
//                        beanName = beanClass.getName().substring(index+1,index+2).toLowerCase() + beanClass.getName().substring(index+2);
//                    }
//                    String scope = beanClass.getAnnotation(Scope.class).scope();
//                    boolean lazy = beanClass.getAnnotation(Lazy.class).lazy();
//                    BeanDefinition beanDefinition = new BeanDefinition();
//                    beanDefinition.setBeanName(beanName);
//                    beanDefinition.setScope(ScopeEnum.prototype.name().equals(scope) ? ScopeEnum.prototype : ScopeEnum.singleton);
//                    beanDefinition.setLazy(lazy);
//                    beanDefinition.setBeanClass(beanClass);
//                    registry.registerBeanDefinition(beanName, beanDefinition);
//                } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
//                }
//            }
//        }

    }

    private Set<Class<?>> scanBeanClass(String[] scanPaths) {
        Set<Class<?>> beanClassSet = new HashSet<>();
        for(String scanPath : scanPaths) {
            scanPath = scanPath.replaceAll("\\.", "/");
            URL classUrl = ClassLoader.getSystemClassLoader().getResource(scanPath);
            File classUrlFile = new File(classUrl.getFile());
            if(classUrlFile.isFile() && classUrlFile.getAbsolutePath().endsWith(".class")) {
                beanClassSet.add(scanFile2Class(classUrlFile));
            } else if(classUrlFile.isDirectory()) {
                for(File classFile : classUrlFile.listFiles()) {
                    if(classFile.isFile() && classFile.getAbsolutePath().endsWith(".class")) {
                        beanClassSet.add(scanFile2Class(classFile));
                    }
                }
            }
        }
        return beanClassSet;
    }

    private Class<?> scanFile2Class(File classUrlFile) {
        String basePath = this.formatPath(AnnotationConfigApplicationContext.class.getClassLoader().getResource("").getPath());
        String fileAbsolutePath = this.formatPath(classUrlFile.getAbsolutePath());
        String fileName = fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf(basePath)+basePath.length(), fileAbsolutePath.lastIndexOf(".class"));
        fileName = fileName.replace(File.separatorChar, '.');
        Class<?> clazz = null;
        try {
            clazz = ClassLoader.getSystemClassLoader().loadClass(fileName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return clazz;
    }

    private String formatPath(String path) {
        if (path == null) {
            return path;
        }
        if (path.length() == 0 || path.trim().length() == 0) {
            return path.trim();
        }
        if (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1);
        }
        path = path.replaceAll("/", Matcher.quoteReplacement(File.separator));
        path = path.replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
        return path;
    }

    private void getImport() {

    }

    private void collectImport(BeanDefinitionRegistry registry, Class<?> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (Objects.equals(Import.class.getName(), annotation.annotationType().getName())) {
                Class<?> importClass = ((Import) annotation).value();

            }
        }
    }
}
