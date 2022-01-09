package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.ClassFilter;
import org.tryImpl.framework.aop.MethodMatcher;
import org.tryImpl.framework.aop.Pointcut;

import java.lang.reflect.Method;

public abstract class TransactionAttributeSourcePointcut implements Pointcut, MethodMatcher {
    private ClassFilter classFilter;

    public TransactionAttributeSourcePointcut (){}

    @Override
    public boolean matches(Method method) {
        if (method == null) {
            return false;
        }
        TransactionAttributeSource transactionAttributeSource = this.getTransactionAttributeSource();
        //FIXME 为什么spring认为“transactionAttributeSource == null”也表示匹配成功？
        return transactionAttributeSource == null || transactionAttributeSource.getTransactionAttribute(method, method.getClass()) != null;
    }

    @Override
    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    protected abstract TransactionAttributeSource getTransactionAttributeSource();
}
