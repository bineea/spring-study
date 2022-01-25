package org.tryImpl.framework.transaction;

import org.tryImpl.framework.annotation.Transactional;

public class DefaultTransactionAttribute implements TransactionAttribute,TransactionDefinition {

    private Class<? extends Throwable>[] rollbackFor;

    public DefaultTransactionAttribute() {}

    public DefaultTransactionAttribute(Transactional transactional) {
        this.rollbackFor = transactional.rollbackFor();
    }

    public Class<? extends Throwable>[] getRollbackFor() {



        return rollbackFor;
    }

    public void setRollbackFor(Class<? extends Throwable>[] rollbackFor) {
        this.rollbackFor = rollbackFor;
    }
}
