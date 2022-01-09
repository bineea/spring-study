package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.MethodInvocation;
import org.tryImpl.framework.intercept.MethodInterceptor;

public class TransactionInterceptor implements MethodInterceptor {

    private TransactionManager transactionManager;

    private TransactionAttributeSource transactionAttributeSource;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //TODO 获取数据库连接，通过beanFactory获取数据库连接池
        try {
            methodInvocation.proceed();
        } catch (Exception exception) {
            //TODO rollback
            return null;
        }
        //TODO commit
        return null;
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
