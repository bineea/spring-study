package com.example.IspringTest.dao;

import org.tryImpl.framework.annotation.Component;
import org.tryImpl.framework.transaction.TransactionSynchronizationManager;

@Component
public class SimpleTestDaoImpl implements SimpleTestDao {

    private TransactionSynchronizationManager transactionSynchronizationManager;

    @Override
    public int updateById(long id) {
        return 0;
    }
}
