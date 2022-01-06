package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.Advice;
import org.tryImpl.framework.aop.Pointcut;
import org.tryImpl.framework.aop.PointcutAdvisor;

public class BeanFactoryTransactionAttributeSourceAdvisor implements PointcutAdvisor {

    private TransactionAttributeSource transactionAttributeSource;
    private TransactionAttributeSourcePointcut transactionAttributeSourcePointcut = new TransactionAttributeSourcePointcut() {

        @Override
        protected TransactionAttributeSource getTransactionAttributeSource() {
            return BeanFactoryTransactionAttributeSourceAdvisor.this.transactionAttributeSource;
        }
    };

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }

    @Override
    public Pointcut getPointcut() {
        return this.transactionAttributeSourcePointcut;
    }

    @Override
    public Advice getAdvice() {
        return null;
    }
}
