package org.tryImpl.framework.transaction;

public interface PlatformTransactionManager extends TransactionManager {

    TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws Exception;

    void commit(TransactionStatus transactionStatus) throws Exception;

    void rollback(TransactionStatus transactionStatus) throws Exception;
}
