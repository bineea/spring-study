package org.tryImpl.framework.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 配置类手动注册为Spring Bean，配置DataSource数据源数据
 */
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
        TransactionDefinition def = (transactionDefinition != null ? transactionDefinition : new DefaultTransactionDefinition());
        Object transaction = doGetTransaction();
        return startTransaction(def, transaction);
    }

    private DataSourceTransactionObject doGetTransaction() {
        return new DataSourceTransactionObject();
    }

    private TransactionStatus startTransaction(TransactionDefinition definition, Object transaction) {
        DefaultTransactionStatus status = new DefaultTransactionStatus(transaction);

        try {
            Connection connection = getDataSource().getConnection();
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public void commit(TransactionStatus transactionStatus) throws Exception {
        //TODO
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) transactionStatus;
        doCommit(defStatus);
    }

    private void doCommit(DefaultTransactionStatus defStatus) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) defStatus.getTransaction();
        Connection currentConnection = txObject.getCurrentConnection();

        try {
            currentConnection.commit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback(TransactionStatus transactionStatus) throws Exception {
        //TODO
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) transactionStatus;
        doRollback(defStatus);
    }

    private void doRollback(DefaultTransactionStatus defStatus) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) defStatus.getTransaction();
        Connection currentConnection = txObject.getCurrentConnection();

        try {
            currentConnection.rollback();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static class DataSourceTransactionObject {
        private Connection currentConnection;

        public Connection getCurrentConnection() {
            return currentConnection;
        }

        public void setCurrentConnection(Connection currentConnection) {

            if (currentConnection != null) {
                //TODO 释放连接
                try {
                    currentConnection.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            this.currentConnection = currentConnection;
        }
    }
}
