package com.example.IspringTest.service;

import com.example.IspringTest.dao.SimpleTestDaoImpl;
import com.example.IspringTest.entity.SampleTestDO;
import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.annotation.Component;
import org.tryImpl.framework.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private SimpleTestDaoImpl simpleTestDaoImpl;

    @Transactional
    @Override
    public void tryTransactionalOperate() {
        System.out.println("执行com.example.IspringTest.service.TransactionalServiceImpl.tryTransactionalOperate。。。");
        SampleTestDO sampleTestDOA = new SampleTestDO();
        sampleTestDOA.setId(2L);
        sampleTestDOA.setCode(0L);
        sampleTestDOA.setRemark("2021-11-10 21:40:00操作：2" + "2022-04-21 19:00:00操作：2");
        int updateByIdCountA = simpleTestDaoImpl.updateById(sampleTestDOA);
        System.out.println(updateByIdCountA);
        SampleTestDO sampleTestDOB = new SampleTestDO();
        sampleTestDOB.setId(3L);
        sampleTestDOB.setCode(2L);
        sampleTestDOB.setRemark("2021-11-10 21:40:00操作：3" + "2022-04-21 19:00:00操作：3");
        int updateByIdCountB = simpleTestDaoImpl.updateById(sampleTestDOB);
        System.out.println(updateByIdCountB);
        throw new RuntimeException("测试事务异常。。。");
    }

    @Override
    public void handleTransactionalOperate(Long fromId, Long toId, Long code) {

    }
}
