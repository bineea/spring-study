package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.Advice;
import org.tryImpl.framework.aop.Pointcut;
import org.tryImpl.framework.aop.PointcutAdvisor;
import org.tryImpl.framework.context.BeanFactory;

public class BeanFactoryTransactionAttributeSourceAdvisor implements PointcutAdvisor {

    private String adviceBeanName;

    private BeanFactory beanFactory;

    private transient volatile Advice advice;

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
        return this.advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
}
