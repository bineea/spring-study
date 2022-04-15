package com.example.IspringTest.service;

import com.example.IspringTest.dao.SimpleTestDaoImpl;
import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.annotation.Component;
import org.tryImpl.framework.annotation.Transactional;

@Component
public class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private SimpleTestDaoImpl simpleTestDaoImpl;

    @Transactional
    @Override
    public void tryTransactionalOperate() {

    }
}
