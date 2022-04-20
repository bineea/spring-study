package org.tryImpl.framework.transaction;

import org.tryImpl.framework.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationTransactionAttributeSource implements TransactionAttributeSource {

    private final static TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute();

    private final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap<>(1024);

    private static final String PACKAGE_SEPARATOR = ".";

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> clazz) {
        //忽略Object对象方法
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        MethodClassKey cacheKey = getCacheKey(method, clazz);
        //设置"NULL"对象，避免重复解析
        TransactionAttribute transactionAttribute = attributeCache.get(cacheKey);
        if (transactionAttribute != null) {
            if (transactionAttribute == NULL_TRANSACTION_ATTRIBUTE) {
                return null;
            } else {
                return transactionAttribute;
            }
        } else {
            transactionAttribute = computeTransactionAttribute(method, clazz);
            if (transactionAttribute == null) {
                attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
            } else {
                attributeCache.put(cacheKey, transactionAttribute);
            }
        }
        return transactionAttribute;
    }

    private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) {

        if (targetClass != null && method.getDeclaringClass() != targetClass && isOverridable(method, targetClass)) {
            try {
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        Method superMethod = null;
        for (Class<?> superclass = targetClass.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            //获取公有和私有全部方法信息
            Method[] methods = superclass.isInterface() ? superclass.getMethods() : superclass.getDeclaredMethods();
            //Method Class.getMethod(String name, Class<?>... parameterTypes)只能获取对象所声明的公开方法
            //Method superMethod = superclass.getMethod(method.getName(), method.getParameterTypes());
            for (Method met : methods) {
                //通过Arrays比较数组数据是否一致（本质是基于对象的equals方法，循环比对数组中的每个对象）
                if (Objects.equals(met.getName(), method.getName()) && (method.getParameterTypes() == null || Arrays.equals(met.getParameterTypes(), method.getParameterTypes()))) {
                    superMethod = met;
                    break;
                }
            }
            if (superMethod != null) {
                break;
            }
        }

        if (superMethod != null) {
            TransactionAttribute transactionAttribute = this.findTransactionAttribute(superMethod);
            if (transactionAttribute != null) {
                return transactionAttribute;
            }
        }

        return this.findTransactionAttribute(method);
    }

    private TransactionAttribute findTransactionAttribute(Method method) {
        if (method.isAnnotationPresent(Transactional.class)) {
            return this.parseTransactionAnnotation(method.getAnnotation(Transactional.class));
        }
        return null;
    }

    private MethodClassKey getCacheKey(Method method, Class<?> clazz) {
        return new MethodClassKey(method, clazz);
    }

    private TransactionAttribute parseTransactionAnnotation(Transactional transactional) {
        DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
        List<String> rollbackRules = defaultTransactionAttribute.getRollbackRules();
        for (Class<? extends Throwable> rollbackForClass : transactional.rollbackFor()) {
            rollbackRules.add(rollbackForClass.getName());
        }
        return defaultTransactionAttribute;
    }

    private boolean isOverridable(Method method, Class<?> targetClass) {
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            return true;
        }
        return (targetClass == null ||
                getPackageName(method.getDeclaringClass().getName()).equals(getPackageName(targetClass.getName())));
    }

    private String getPackageName(String fqClassName) {
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    class MethodClassKey {
        private final Method method;

        private final Class<?> targetClass;

        MethodClassKey(Method method, Class<?> targetClass) {
            this.method = method;
            this.targetClass = targetClass;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}
