package org.tryImpl.framework.annotation;

import org.tryImpl.framework.transaction.BeanFactoryTransactionAttributeSourceAdvisor;
import org.tryImpl.framework.transaction.TransactionAttributeSource;
import org.tryImpl.framework.transaction.TransactionInterceptor;
import org.tryImpl.framework.transaction.TransactionManager;

@Configuration
public class ProxyTransactionManagementConfiguration {

    public static final String TRANSACTION_ADVISOR_BEAN_NAME =
            "org.springframework.transaction.config.internalTransactionAdvisor";


    private TransactionManager txManager;

    @Bean(name = TRANSACTION_ADVISOR_BEAN_NAME)
    public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(
            TransactionAttributeSource transactionAttributeSource, TransactionInterceptor transactionInterceptor) {
        //TODO 注入@Transaction处理advisor
        return null;
    }

    @Bean(name = "")
    public TransactionAttributeSource transactionAttributeSource() {
        //TODO
        return null;
    }

    @Bean(name = "")
    public TransactionInterceptor transactionInterceptor(TransactionAttributeSource transactionAttributeSource) {
        //TODO
        return null;
    }

}
