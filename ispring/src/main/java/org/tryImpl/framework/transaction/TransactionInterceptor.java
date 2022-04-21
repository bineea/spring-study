package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.MethodInvocation;
import org.tryImpl.framework.aop.ReflectiveMethodInvocation;
import org.tryImpl.framework.intercept.MethodInterceptor;

public class TransactionInterceptor extends TransactionAspectSupport implements MethodInterceptor {

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
        if (transactionManager == null) {
            transactionManager = (TransactionManager) getBeanFactory().getBean(TransactionManager.class.getName().toLowerCase());
        }
        if (transactionManager instanceof PlatformTransactionManager) {
            ptm = (PlatformTransactionManager) transactionManager;
        } else {
            throw new RuntimeException("目前只支持PlatformTransactionManager类型事务管理器");
        }

        ReflectiveMethodInvocation reflectiveMethodInvocation = null;
        if (methodInvocation instanceof ReflectiveMethodInvocation) {
            reflectiveMethodInvocation = (ReflectiveMethodInvocation) methodInvocation;
        } else {
            throw new RuntimeException("目前只支持ReflectiveMethodInvocation类型代理调用信息");
        }
        TransactionAttribute transactionAttribute = transactionAttributeSource.getTransactionAttribute(reflectiveMethodInvocation.getMethod(), reflectiveMethodInvocation.getTarget().getClass());
        TransactionStatus status = ptm.getTransaction(transactionAttribute);
        Object retVal;
        try {
            //触发下一个后置处理器方法
            retVal = methodInvocation.proceed();
        } catch (Throwable throwable) {
            ptm.rollback(status);
            throw new RuntimeException(throwable);
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
