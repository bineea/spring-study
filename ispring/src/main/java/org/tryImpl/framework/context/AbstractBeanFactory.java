package org.tryImpl.framework.context;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.processor.BeanPostProcessor;
import org.tryImpl.framework.processor.InstantiationAwareBeanPostProcessor;
import org.tryImpl.framework.support.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            Object beanInstance = null;
            if (beanDefinition.getFactoryMethodName() != null && beanDefinition.getFactoryMethodName().trim().length() > 0) {
                //执行方法创建bean对象
                beanInstance = this.instantiateUsingFactoryMethod(beanDefinition.getBeanName(), beanDefinition);
                //spring中不是补全beanDefinition的beanClass属性，而是通过org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor#buildAutowiringMetadata
                //解析class标注@Autowired注解的属性，并进行赋值操作
                beanDefinition.setBeanClass(beanInstance.getClass());
                return beanInstance;
            }
            beanInstance = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
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
                currentBean = beanInstance;
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
     * 通过工厂方法实例化bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object instantiateUsingFactoryMethod(String beanName, BeanDefinition beanDefinition) {
        String factoryBeanName = beanDefinition.getFactoryBeanName();
        Object factoryBean;
        Class<?> factoryClass;
        if (factoryBeanName != null && factoryBeanName.trim().length() > 0) {
            if (factoryBeanName.equals(beanName)) {
                throw new RuntimeException("创建"+beanName+"Bean工厂与当前Bean一致");
            }
            factoryBean = this.getBean(factoryBeanName);
            factoryClass = factoryBean.getClass();
        } else {
            throw new RuntimeException("创建"+beanName+"暂不支持静态工厂方法创建Bean对象");
        }

        Method factoryMethodToUse = null;
        Object[] argsToUse = null;

        Method[] rawCandidates = factoryClass.getDeclaredMethods();
        HashSet<Method> candidateSet = new HashSet<>();
        for (Method candidate : rawCandidates) {
            //根据方法名称过滤方法对象，因为JAVA方法存在重载，也就意味着同一个方法名称可以对应多个参数不同的方法
            if (candidate.getName().equals(beanDefinition.getFactoryMethodName())) {
                candidateSet.add(candidate);
            }
        }

        for (Method candidate : candidateSet) {
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            String[] paramNames = getParameterNames(candidate);
            ArgumentsHolder argumentsHolder = this.createArgumentArray(beanName, beanDefinition, parameterTypes, paramNames, candidate);

            //比对参数类型与参数值是否匹配
            if (parameterTypes.length > 0) {
                for (int i=0; i<parameterTypes.length; i++) {
                    if (argumentsHolder.arguments == null) {
                        break;
                    }
                    if (argumentsHolder.arguments.length - 1 < i) {
                        break;
                    }
                    if (!parameterTypes[i].equals(argumentsHolder.arguments[i].getClass())
                            && !parameterTypes[i].isAssignableFrom(argumentsHolder.arguments[i].getClass())) {
                        break;
                    }
                    if (i == parameterTypes.length - 1) {
                        factoryMethodToUse = candidate;
                        argsToUse = argumentsHolder.arguments;
                    }
                }
            } else {
                //基本等价于spring的处理
                //根据参数类型和参数值计算方法匹配度，如果方法参数为空，则将当前方法直接赋值给factoryMethodToUse
                factoryMethodToUse = candidate;
            }
        }


        if (factoryMethodToUse == null) {
            throw new RuntimeException("创建"+beanName+"无法匹配工厂方法");
        }
        if (Void.TYPE == factoryMethodToUse.getReturnType()) {
            throw new RuntimeException("创建"+beanName+"无效工厂方法");
        }

        try {
            Object beanInstance = factoryMethodToUse.invoke(factoryBean, argsToUse);
            return beanInstance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建"+beanName+"工厂方法创建Bean对象异常", e);
        }
    }

    /**
     * 获取方法参数名称
     * @param method
     * @return
     */
    private String[] getParameterNames(Method method) {
        //桥接方法是 JDK 1.5 引入泛型后，为了使Java的泛型方法生成的字节码和 1.5 版本前的字节码相兼容，由编译器自动生成的方法。
        //子类在继承（或实现）父类（或接口）的泛型方法时，在子类中明确指定了泛型类型，那么在编译时编译器会自动生成桥接方法（当然还有其他情况会生成桥接方法，这里只是列举了其中一种情况）
        if (method.isBridge()) {
            throw new RuntimeException("暂不支持桥接方法获取方法参数");
        }

        //JAVA反射无法获取方法参数的名称，只能通过字节码文件的本地变量表信息获取！
        try {
            List<String> methodParamNames = LocalVariableTableParameterNameDiscoverer.getMethodParamNames(method.getDeclaringClass(), method);
            return methodParamNames.toArray(new String[methodParamNames.size()]);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取工厂方法所有参数
     * @param paramTypes
     * @param paramNames
     * @return
     */
    private ArgumentsHolder createArgumentArray(String beanName, BeanDefinition beanDefinition, Class<?>[] paramTypes, String[] paramNames, Method method) {
        ArgumentsHolder argumentsHolder = new ArgumentsHolder(paramTypes.length);
        for (int i=0; i<paramTypes.length; i++) {
            Object argument = this.resolveAutowiredArgument(paramNames[i]);
            argumentsHolder.arguments[i] = argument;
        }
        return argumentsHolder;
    }

    /**
     * 获取参数对象
     * @return
     */
    private Object resolveAutowiredArgument(String beanName) {
        Object bean = getBean(beanName);
        return bean;
    }

    private static class ArgumentsHolder {

        public final Object[] arguments;

        ArgumentsHolder (int size) {
            arguments = new Object[size];
        }


    }
}
