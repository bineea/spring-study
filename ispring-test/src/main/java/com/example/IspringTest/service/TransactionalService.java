package com.example.IspringTest.service;

import java.math.BigDecimal;

public interface TransactionalService {

    void tryTransactionalOperate();

    void handleTransactionalOperate(Long fromId, Long toId, Long code);
}
