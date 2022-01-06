package org.tryImpl.framework.transaction;

import java.lang.reflect.Method;

public interface TransactionAttributeSource {

    TransactionAttribute getTransactionAttribute(Method method, Class<?> clazz);
}
