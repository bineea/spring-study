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
        TransactionDefinition def = transactionDefinition != null ? transactionDefinition : TransactionDefinition.withDefaults();
        Object transaction = doGetTransaction();
        if (isExistingTransaction(transaction)) {
            // Existing transaction found -> check propagation behavior to find out how to behave.
            return handleExistingTransaction(def, transaction);
        }
        return startTransaction(def, transaction);
    }

    private boolean isExistingTransaction(Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
        return txObject.getCurrentConnection() != null && txObject.isTransactionActive() == true;
    }

    private TransactionStatus handleExistingTransaction(TransactionDefinition definition, Object transaction) {
        DefaultTransactionStatus status = new DefaultTransactionStatus(transaction);
        return status;
    }

    private DataSourceTransactionObject doGetTransaction() {
        DataSourceTransactionObject txObject = new DataSourceTransactionObject();
        Connection connection = (Connection) TransactionSynchronizationManager.getResource(getDataSource());
        txObject.setCurrentConnection(connection);
        txObject.setNewConnection(false);
        return txObject;
    }

    private TransactionStatus startTransaction(TransactionDefinition definition, Object transaction) {
        DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;

        DefaultTransactionStatus status = new DefaultTransactionStatus(transaction);
        Connection connection = null;
        try {
            if (txObject.getCurrentConnection() == null || txObject.isSynchronizedWithTransaction()) {
                //FIXME 数据库连接获取到自动关闭。。。
                Connection newCon = getDataSource().getConnection();
                txObject.setCurrentConnection(newCon);
                txObject.setNewConnection(true);
            }
            txObject.setSynchronizedWithTransaction(true);
            connection = txObject.getCurrentConnection();
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }

            if (txObject.isNewConnection()) {
                TransactionSynchronizationManager.bindResource(getDataSource(), connection);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public void commit(TransactionStatus transactionStatus) throws Exception {
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
        private boolean synchronizedWithTransaction = false;
        private Connection currentConnection;
        private boolean transactionActive = false;

        private boolean newConnection;

        public boolean isSynchronizedWithTransaction() {
            return synchronizedWithTransaction;
        }

        public void setSynchronizedWithTransaction(boolean synchronizedWithTransaction) {
            this.synchronizedWithTransaction = synchronizedWithTransaction;
        }

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

        public boolean isTransactionActive() {
            return transactionActive;
        }

        public void setTransactionActive(boolean transactionActive) {
            this.transactionActive = transactionActive;
        }

        public boolean isNewConnection() {
            return newConnection;
        }

        public void setNewConnection(boolean newConnection) {
            this.newConnection = newConnection;
        }
    }
}
