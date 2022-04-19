package org.tryImpl.framework.transaction;

import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.BeanFactoryAware;
import org.tryImpl.framework.context.InitializingBean;

//FIXME 通过动态代理，获取transactionManager对应的bean，并注册到transactionInterceptor中，用于获取数据库连接
public abstract class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {

    private BeanFactory beanFactory;

    private TransactionManager transactionManager;

    private TransactionAttributeSource transactionAttributeSource;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
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

    @Override
    public void afterPropertiesSet() {
        if (getTransactionManager() == null && this.beanFactory == null) {
            throw new IllegalStateException(
                    "Set the 'transactionManager' property or make sure to run within a BeanFactory " +
                            "containing a TransactionManager bean!");
        }
        if (getTransactionAttributeSource() == null) {
            throw new IllegalStateException(
                    "Either 'transactionAttributeSource' or 'transactionAttributes' is required: " +
                            "If there are no transactional methods, then don't use a transaction aspect.");
        }
    }

}
