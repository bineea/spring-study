package org.tryImpl.framework.annotation;

import org.tryImpl.framework.transaction.*;

@Configuration
public class ProxyTransactionManagementConfiguration {

    public static final String TRANSACTION_ADVISOR_BEAN_NAME =
            "org.springframework.transaction.config.internalTransactionAdvisor";

    //TODO
    private TransactionManager txManager;

    //注入@Transaction处理advisor
    @Bean(name = TRANSACTION_ADVISOR_BEAN_NAME)
    public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(
            TransactionAttributeSource transactionAttributeSource, TransactionInterceptor transactionInterceptor) {
        BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
        advisor.setTransactionAttributeSource(transactionAttributeSource);
        advisor.setAdvice(transactionInterceptor);
        return advisor;
    }

    @Bean(name = "transactionAttributeSource")
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource();
    }

    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(TransactionAttributeSource transactionAttributeSource) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource);
        if (txManager != null) {
            transactionInterceptor.setTransactionManager(txManager);
        }
        return transactionInterceptor;
    }

}
