package org.tryImpl.framework.processor;

import org.tryImpl.framework.annotation.*;
import org.tryImpl.framework.context.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;

public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private Set<ComponentScan> componentScanSet = new HashSet<>();

    private final ArrayDeque<ConfigurationClass> importStack = new ArrayDeque<>();
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
            this.parse(registry, new ConfigurationClass(beanName, registry.getBeanDefinition(beanName).getBeanClass()));
        }
    }

    private void parse(BeanDefinitionRegistry registry, ConfigurationClass configurationClass) {
        if (this.shouldSkip(configurationClass.getClazz())) {
            return;
        }
        this.processConfigurationClass(registry, configurationClass);
    }

    private void processConfigurationClass(BeanDefinitionRegistry registry, ConfigurationClass configurationClass) {
        Class<?> clazz = configurationClass.getClazz();

        if (!configurationClass.getImportedBy().isEmpty()) {
            int index = clazz.getName().lastIndexOf(".");
            String beanName = clazz.getName().substring(index+1,index+2).toLowerCase() + clazz.getName().substring(index+2);

            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanName(beanName);
            beanDefinition.setScope(ScopeEnum.singleton);
            beanDefinition.setLazy(false);
            beanDefinition.setBeanClass(clazz);
            registry.registerBeanDefinition(beanName, beanDefinition);

            configurationClass.setBeanName(beanName);
        }

        //解析componentScan注解
        //spring只解析Configuration注解标注配置的componentScan注解
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            if (componentScanSet.contains(componentScan)) {
                return;
            }
            componentScanSet.add(componentScan);
            String[] scanPaths = componentScan.value();
            Set<Class<?>> beanClasses = this.scanBeanClass(scanPaths);
            for (Class<?> beanClass : beanClasses) {
                int index = beanClass.getName().lastIndexOf(".");
                String beanName = beanClass.getName().substring(index+1,index+2).toLowerCase() + beanClass.getName().substring(index+2);
                if (beanClass.isAnnotationPresent(Component.class)) {
                    String customBeanName = beanClass.getAnnotation(Component.class).value();
                    if (customBeanName != null && customBeanName.trim().length() > 0) {
                        beanName = customBeanName;
                    }
                }
                if (registry.getBeanDefinition(beanName) != null) {
                    continue;
                }
                if (beanClass.isAnnotationPresent(Configuration.class) || beanClass.isAnnotationPresent(Component.class)) {
                    String scope = beanClass.getAnnotation(Scope.class) == null ? ScopeEnum.singleton.name() : beanClass.getAnnotation(Scope.class).scope();
                    boolean lazy = beanClass.getAnnotation(Lazy.class) == null ? false : beanClass.getAnnotation(Lazy.class).lazy();
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setBeanName(beanName);
                    beanDefinition.setScope(ScopeEnum.prototype.name().equals(scope) ? ScopeEnum.prototype : ScopeEnum.singleton);
                    beanDefinition.setLazy(lazy);
                    beanDefinition.setBeanClass(beanClass);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                if (beanClass.isAnnotationPresent(Configuration.class)) {
                    this.parse(registry, new ConfigurationClass(beanName, beanClass));
                }
            }
        }

        //解析import注解
        processImports(registry, configurationClass, this.getImports(configurationClass.getClazz()));

        //解析bean注解
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                try {
                    //这里只是解析了bean的方法信息
                    //1.方法名称设置为beanName
                    //2.设置beanDefinition的factoryBeanName属性为配置类的beanName(appConfig)
                    //3.设置beanDefinition的factoryMethodName属性为bean method(getCreateHelloworldManager)
                    //4.设置beanDefinition为org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.ConfigurationClassBeanDefinition
                    this.loadBeanDefinitionsForBeanMethod(registry, configurationClass, method);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private boolean shouldSkip(Class<?> clazz) {
        return !clazz.isAnnotationPresent(Configuration.class);
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

    private void processImports(BeanDefinitionRegistry registry, ConfigurationClass currentConfigurationClass, Set<Class<?>> imports) {
        if (imports == null || imports.isEmpty()) {
            return;
        }

        for (Class<?> importClass : imports) {

            ConfigurationClass configurationClass = new ConfigurationClass(importClass);
            if (importStack.contains(configurationClass)) {
                continue;
            }
            importStack.push(configurationClass);
            if (ImportSelector.class.isAssignableFrom(importClass)) {
                try {
                    ImportSelector importSelector = (ImportSelector) importClass.getDeclaredConstructor().newInstance();
                    HashSet<Class<?>> importSelectClasses = new HashSet<>();
                    for (String importClassName : importSelector.selectImports(currentConfigurationClass.getClass().getAnnotations())) {
                        Class<?> importSelectClass = Class.forName(importClassName);
                        importSelectClasses.add(importSelectClass);
                    }
                    processImports(registry, currentConfigurationClass, importSelectClasses);
                } catch (Exception e)  {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else if (ImportBeanDefinitionRegistrar.class.isAssignableFrom(importClass)) {
                try {
                    //手动初始化，执行beanDefinition的注册
                    ImportBeanDefinitionRegistrar importInstance = (ImportBeanDefinitionRegistrar) importClass.getDeclaredConstructor().newInstance();
                    importInstance.registerBeanDefinitions(registry);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else {
                //候选类不是ImportSelector或ImportBeanDefinitionRegister，则将其作为Configuration类处理
                Set<ConfigurationClass> importedBy = configurationClass.getImportedBy();
                importedBy.add(currentConfigurationClass);
                parse(registry, configurationClass);
            }
            importStack.pop();
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

    private void loadBeanDefinitionsForBeanMethod(BeanDefinitionRegistry registry, ConfigurationClass configurationClass, Method method) {
        if (!method.isAnnotationPresent(Bean.class)) {
            throw new RuntimeException("Bean工厂方法没有@Bean注解");
        }
        String beanName = method.getDeclaredAnnotation(Bean.class).name();
        beanName = (beanName == null || beanName.trim().length() <= 0) ? method.getName() : beanName;
        ConfigurationClassBeanDefinition beanDef = new ConfigurationClassBeanDefinition();
        beanDef.setMethodName(method.getName());
        beanDef.setReturnTypeName(method.getReturnType().getName());
        beanDef.setBeanName(beanName);
        beanDef.setScope(ScopeEnum.singleton);
        beanDef.setLazy(Boolean.FALSE);
        beanDef.setFactoryBeanName(configurationClass.getBeanName());
        beanDef.setFactoryMethodName(method.getName());
        beanDef.setFactoryMethodReturnType(method.getReturnType());
        registry.registerBeanDefinition(beanName, beanDef);
    }

    class ConfigurationClassBeanDefinition extends RootBeanDefinition {
        private String methodName;
        private String returnTypeName;

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getReturnTypeName() {
            return returnTypeName;
        }

        public void setReturnTypeName(String returnTypeName) {
            this.returnTypeName = returnTypeName;
        }
    }
}
