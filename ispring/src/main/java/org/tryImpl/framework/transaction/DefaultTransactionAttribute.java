package org.tryImpl.framework.transaction;

import org.tryImpl.framework.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class DefaultTransactionAttribute implements TransactionAttribute,TransactionDefinition {

    private static final String ROLLBACK_ON_RUNTIME_EXCEPTIONS = RuntimeException.class.getName();

    private List<String> rollbackRules;

    public DefaultTransactionAttribute() {
        this.rollbackRules = new ArrayList<>();
    }

    public List<String> getRollbackRules() {
        return rollbackRules;
    }

    public void setRollbackRules(List<String> rollbackRules) {
        this.rollbackRules = rollbackRules;
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        //TODO 参考RuleBasedTransactionAttribute
        return (ex instanceof RuntimeException || ex instanceof Error);
    }
}
