package org.tryImpl.framework.context;

import org.tryImpl.framework.annotation.*;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private Set<Class<?>> classSet;

    private AnnotationConfigApplicationContext() {}

    public AnnotationConfigApplicationContext(Class<?>... classes) {
        this.init(classes);
        super.create();
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
    protected void registerBeanDefinition() {
        try {
            Set<Class<?>> beanClasses = this.getBeanClass();
            for(Class<?> beanClass : beanClasses) {
                String beanName = null;
                String scope = beanClass.getAnnotation(Scope.class).scope();
                boolean lazy = beanClass.getAnnotation(Lazy.class).lazy();
                if(beanClass.isAnnotationPresent(Configuration.class)) {
                    beanName = null;
                    scope = ScopeEnum.singleton.name();
                    lazy = false;
                }
                if(beanClass.isAnnotationPresent(Component.class)) {
                    beanName = beanClass.getAnnotation(Component.class).value();
                }
                if(beanName == null || beanName.trim().length() <= 0) {
                    beanName = beanName.substring(beanName.lastIndexOf('.')+1);
                    beanName.replaceFirst(String.valueOf(beanName.charAt(0)), String.valueOf(beanName.charAt(0)).toLowerCase());
                }
                BeanDefinition beanDefinition = new BeanDefinition();

                beanDefinition.setBeanName(beanName);
                beanDefinition.setScope(ScopeEnum.prototype.name().equals(scope) ? ScopeEnum.prototype : ScopeEnum.singleton);
                beanDefinition.setLazy(lazy);
                beanDefinition.setBeanClass(beanClass);
                beanNameList.add(beanName);
                beanDefinitionMap.put(beanClass.getName(),beanDefinition);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Set<Class<?>> getBeanClass() throws ClassNotFoundException {
        Set<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(classSet);
        for(Class<?> clazz : classSet) {
            if(clazz.isAnnotationPresent(ComponentScan.class)) {
                String[] scanPaths = clazz.getAnnotation(ComponentScan.class).value();
                beanClassSet.addAll(this.scanBeanClass(scanPaths));
            }
        }
        return beanClassSet;
    }

    private Set<Class<?>> scanBeanClass(String[] scanPaths) throws ClassNotFoundException {
        Set<Class<?>> beanClassSet = new HashSet<>();
        for(String scanPath : scanPaths) {
            scanPath = scanPath.replaceAll(".", "/");
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

    private Class<?> scanFile2Class(File classUrlFile) throws ClassNotFoundException {
        String basePath = AnnotationConfigApplicationContext.class.getClassLoader().getResource("/").getPath();
        String fileAbsolutePath = classUrlFile.getAbsolutePath();
        String fileName = fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf(basePath), fileAbsolutePath.lastIndexOf(".class"));
        fileName = fileName.replace(File.separatorChar, '.');
        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(fileName);
        return clazz;
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
