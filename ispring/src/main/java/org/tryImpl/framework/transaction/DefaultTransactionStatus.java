package org.tryImpl.framework.transaction;

//TODO
public class DefaultTransactionStatus implements TransactionStatus {

    private final Object transaction;

    public DefaultTransactionStatus(Object transaction) {
        this.transaction = transaction;
    }

    public Object getTransaction() {
        return transaction;
    }

    @Override
    public boolean isNewTransaction() {
        return false;
    }

    @Override
    public boolean hasSavepoint() {
        return false;
    }

    @Override
    public void setRollbackOnly() {

    }

    @Override
    public boolean isRollbackOnly() {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
