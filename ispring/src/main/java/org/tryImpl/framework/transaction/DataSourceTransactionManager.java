package org.tryImpl.framework.transaction;

import javax.sql.DataSource;

public class DataSourceTransactionManager implements TransactionManager {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
