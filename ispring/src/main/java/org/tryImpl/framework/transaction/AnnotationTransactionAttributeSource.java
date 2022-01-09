package org.tryImpl.framework.transaction;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationTransactionAttributeSource implements TransactionAttributeSource {

    private final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap<>(1024);

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> clazz) {
        MethodClassKey cacheKey = getCacheKey(method, clazz);
        TransactionAttribute transactionAttribute = attributeCache.get(cacheKey);
        if (transactionAttribute != null) {
            return transactionAttribute;
        }
        //TODO



        return null;
    }

    private TransactionAttribute computeTransactionAttribute(Method method, Class<?> clazz) {
        //TODO
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
