package org.tryImpl.framework.transaction;

public interface TransactionAttribute extends TransactionDefinition {

    boolean rollbackOn(Throwable ex);
}
