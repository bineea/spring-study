package org.tryImpl.framework.transaction;

final class StaticTransactionDefinition implements TransactionDefinition {

    final static StaticTransactionDefinition INSTANCE = new StaticTransactionDefinition();

    private StaticTransactionDefinition() {}
}
