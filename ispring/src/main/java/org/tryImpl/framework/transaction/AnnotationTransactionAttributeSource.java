package org.tryImpl.framework.transaction;

import org.tryImpl.framework.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationTransactionAttributeSource implements TransactionAttributeSource {

    private final static TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute();

    private final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap<>(1024);

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> clazz) {
        MethodClassKey cacheKey = getCacheKey(method, clazz);
        //设置"NULL"对象，避免重复解析
        TransactionAttribute transactionAttribute = attributeCache.get(cacheKey);
        if (transactionAttribute == null) {
            transactionAttribute = computeTransactionAttribute(method, clazz);
            if (transactionAttribute == null) {
                transactionAttribute = NULL_TRANSACTION_ATTRIBUTE;
            }
            attributeCache.put(cacheKey, transactionAttribute);
        }
        return transactionAttribute;
    }

    private TransactionAttribute computeTransactionAttribute(Method method, Class<?> clazz) {
        Method superMethod = null;
        for (Class<?> superclass = clazz.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
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

        TransactionAttribute transactionAttribute = this.findTransactionAttribute(superMethod);
        if (transactionAttribute != null) {
            return transactionAttribute;
        } else {
            return this.findTransactionAttribute(method);
        }
    }

    private TransactionAttribute findTransactionAttribute(Method method) {
        if (method.isAnnotationPresent(Transactional.class)) {
            return new DefaultTransactionAttribute(method.getAnnotation(Transactional.class));
        }
        return null;
    }

    private MethodClassKey getCacheKey(Method method, Class<?> clazz) {
        return new MethodClassKey(method, clazz);
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
