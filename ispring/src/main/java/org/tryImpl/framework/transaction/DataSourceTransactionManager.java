package org.tryImpl.framework.transaction;

import javax.sql.DataSource;

//TODO
public class DataSourceTransactionManager implements PlatformTransactionManager {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws Exception {
        return null;
    }

    @Override
    public void commit(TransactionStatus transactionStatus) throws Exception {

    }

    @Override
    public void rollback(TransactionStatus transactionStatus) throws Exception {

    }
}
