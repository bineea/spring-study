package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.MethodInvocation;
import org.tryImpl.framework.intercept.MethodInterceptor;

public class TransactionInterceptor implements MethodInterceptor {

    private TransactionManager transactionManager;

    private TransactionAttributeSource transactionAttributeSource;

    public TransactionInterceptor () {}

    public TransactionInterceptor (PlatformTransactionManager ptm, TransactionAttributeSource tas) {
        setTransactionManager(ptm);
        setTransactionAttributeSource(tas);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        PlatformTransactionManager ptm = null;
        if (transactionManager instanceof PlatformTransactionManager) {
            ptm = (PlatformTransactionManager) transactionManager;
        } else {
            throw new RuntimeException("目前只支持PlatformTransactionManager类型事务管理器");
        }
        TransactionAttribute transactionAttribute = transactionAttributeSource.getTransactionAttribute(methodInvocation.getMethod(), methodInvocation.getClass());
        TransactionStatus status = ptm.getTransaction(transactionAttribute);
        Object retVal;
        try {
            retVal = methodInvocation.proceed();
        } catch (Exception exception) {
            ptm.rollback(status);
            throw new RuntimeException(exception);
        }
        ptm.commit(status);
        return retVal;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public TransactionAttributeSource getTransactionAttributeSource() {
        return transactionAttributeSource;
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }
}
