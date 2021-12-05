package org.tryImpl.framework.processor;

import org.tryImpl.framework.annotation.*;
import org.tryImpl.framework.context.AnnotationConfigApplicationContext;
import org.tryImpl.framework.context.BeanDefinition;
import org.tryImpl.framework.context.BeanDefinitionRegistry;
import org.tryImpl.framework.context.ScopeEnum;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;

public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private Set<ComponentScan> componentScanSet = new HashSet<>();

    /**
     * 解析配置类，注册beanDefinition
     * @param registry
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        List<String> beanDefinitionNames = new ArrayList<>();
        for (String beanName : registry.getBeanDefinitionNames()) {
            beanDefinitionNames.add(beanName);
        }
        for (String beanName : beanDefinitionNames) {
            this.parse(registry, registry.getBeanDefinition(beanName).getBeanClass());
        }
    }

    private void parse(BeanDefinitionRegistry registry, Class<?> clazz) {

        //解析componentScan注解
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            if (componentScanSet.contains(componentScan)) {
                return;
            }
            componentScanSet.add(componentScan);
            String[] scanPaths = componentScan.value();
            Set<Class<?>> beanClasses = this.scanBeanClass(scanPaths);
            for (Class<?> beanClass : beanClasses) {
                if (beanClass.isAnnotationPresent(Configuration.class)) {
                    this.parse(registry, beanClass);
                } else if (beanClass.isAnnotationPresent(ComponentScan.class)) {
                    this.parse(registry, beanClass);
                } else if (beanClass.isAnnotationPresent(Component.class)){
                    String beanName = beanClass.getAnnotation(Component.class).value();
                    if (beanName == null || beanName.length() <= 0) {
                        int index = beanClass.getName().lastIndexOf(".");
                        beanName = beanClass.getName().substring(index+1,index+2).toLowerCase() + beanClass.getName().substring(index+2);
                    }
                    String scope = beanClass.getAnnotation(Scope.class) == null ? ScopeEnum.singleton.name() : beanClass.getAnnotation(Scope.class).scope();
                    boolean lazy = beanClass.getAnnotation(Lazy.class) == null ? false : beanClass.getAnnotation(Lazy.class).lazy();
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setBeanName(beanName);
                    beanDefinition.setScope(ScopeEnum.prototype.name().equals(scope) ? ScopeEnum.prototype : ScopeEnum.singleton);
                    beanDefinition.setLazy(lazy);
                    beanDefinition.setBeanClass(beanClass);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }

        //解析import注解
        processImports(registry, clazz);

        //解析bean注解
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
            beanClassSet.addAll(scanFile2Class(classUrlFile));
        }
        return beanClassSet;
    }

    private Set<Class<?>> scanFile2Class(File classUrlFile) {
        Set<Class<?>> beanClassSet = new HashSet<>();
        if(classUrlFile.isFile() && classUrlFile.getAbsolutePath().endsWith(".class")) {
            String basePath = this.formatPath(AnnotationConfigApplicationContext.class.getClassLoader().getResource("").getPath());
            String fileAbsolutePath = this.formatPath(classUrlFile.getAbsolutePath());
            String fileName = fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf(basePath)+basePath.length(), fileAbsolutePath.lastIndexOf(".class"));
            fileName = fileName.replace(File.separatorChar, '.');
            Class<?> clazz = null;
            try {
                clazz = ClassLoader.getSystemClassLoader().loadClass(fileName);
                beanClassSet.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return beanClassSet;
        } else if(classUrlFile.isDirectory()) {
            for(File classFile : classUrlFile.listFiles()) {
                beanClassSet.addAll(scanFile2Class(classFile));
            }
            return beanClassSet;
        } else {
            return beanClassSet;
        }
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

    private void processImports(BeanDefinitionRegistry registry, Class<?> clazz) {
        Set<Class<?>> imports = this.getImports(clazz);
        for (Class<?> importClass : imports) {
            if (ImportBeanDefinitionRegistrar.class.isAssignableFrom(importClass)) {
                try {
                    //手动初始化，执行beanDefinition的注册
                    ImportBeanDefinitionRegistrar importInstance = (ImportBeanDefinitionRegistrar) importClass.getDeclaredConstructor().newInstance();
                    importInstance.registerBeanDefinitions(registry);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Set<Class<?>> getImports(Class<?> clazz) {
        Set<Class<?>> importClasses = new HashSet<>();
        collectImport(importClasses, clazz);
        return importClasses;
    }

    private void collectImport(Set<Class<?>> importClasses, Class<?> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (Objects.equals(Import.class.getName(), annotation.annotationType().getName())) {
                Class<?> importClass = ((Import) annotation).value();
                importClasses.add(importClass);
            } else if (annotation.annotationType().getName().contains("java.lang.annotation")) {
                continue;
            } else {
                this.collectImport(importClasses, annotation.annotationType());
            }
        }
    }
}
