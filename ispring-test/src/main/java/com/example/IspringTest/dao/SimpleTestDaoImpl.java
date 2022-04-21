package com.example.IspringTest.dao;

import com.example.IspringTest.entity.SampleTestDO;
import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.annotation.Component;
import org.tryImpl.framework.transaction.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class SimpleTestDaoImpl implements SimpleTestDao {

    @Autowired
    private DataSource dataSource;

    @Override
    public int updateById(SampleTestDO sampleTestDO) {
        try {
            Connection connection  = (Connection) TransactionSynchronizationManager.getResource(dataSource);

            String sql = "update sample_test set code = ?, remark = ?, version = version + 1 where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, sampleTestDO.getCode());
            preparedStatement.setObject(2, sampleTestDO.getRemark());
            preparedStatement.setObject(3, sampleTestDO.getId());
            return preparedStatement.executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return 0;
    }
}
