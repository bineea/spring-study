package org.tryImpl.framework.transaction;

import javax.sql.DataSource;

public class DataSourceTransactionManager implements PlatformTransactionManager {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSourceTransactionManager(DataSource dataSource) {
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws Exception {
        //TODO
        return null;
    }

    @Override
    public void commit(TransactionStatus transactionStatus) throws Exception {
        //TODO
    }

    @Override
    public void rollback(TransactionStatus transactionStatus) throws Exception {
        //TODO
    }
}
