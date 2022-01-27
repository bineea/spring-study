package org.tryImpl.framework.transaction;

public interface TransactionAttribute {

    boolean rollbackOn(Throwable ex);
}
