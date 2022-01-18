package org.tryImpl.framework.transaction;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationTransactionAttributeSource implements TransactionAttributeSource {

    private final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap<>(1024);

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> clazz) {
        MethodClassKey cacheKey = getCacheKey(method, clazz);
        //TODO 需要设置"NULL"对象，避免重复解析
        TransactionAttribute transactionAttribute = attributeCache.get(cacheKey);
        if (transactionAttribute != null) {
            return transactionAttribute;
        } else {
            transactionAttribute = computeTransactionAttribute(method, clazz);
            attributeCache.put(cacheKey, transactionAttribute);
            return transactionAttribute;
        }
    }

    private TransactionAttribute computeTransactionAttribute(Method method, Class<?> clazz) {
        //TODO
        //1.method是否是public;
        //2.方法或者接口findTransactionAttribute;
        return null;
    }

    private TransactionAttribute findTransactionAttribute(Method method) {
        //TODO
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
