package com.example.IspringTest.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.IspringTest.service.CreateBeanByMethod;
import org.tryImpl.framework.annotation.*;
import org.tryImpl.framework.transaction.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Title: AppConfig<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2021/11/26 11:06
 */

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan({"com.example.IspringTest"})
public class AppConfig {

    @Bean(name = "createBeanByMethod")
    public CreateBeanByMethod createCreateBeanByMethod() {
        return new CreateBeanByMethod();
    }

    @Bean(name = "dataSource")
    public DataSource initDataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://152.136.155.204:3316/sampledb?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true");
        dataSource.setUsername("sample");
        dataSource.setPassword("sample");
        //初始化连接数
        dataSource.setInitialSize(5);
        //最小空闲连接数
        dataSource.setMinIdle(1);
        //最大活动连接数
        dataSource.setMaxActive(100);
        //最大连接等待时间
        dataSource.setMaxWait(60000);
        //SQL查询,用来验证从连接池取出的连接,在将连接返回给调用者之前.如果指定,则查询必须是一个SQL SELECT并且必须返回至少一行记录
        dataSource.setValidationQuery("select 1");
        //是否在从连接池中取出连接前进行检验,如果检验失败,则从连接池中去除连接并尝试取出另一个；若设置为true，validationQuery参数必须设置为非空字符串
        dataSource.setTestOnBorrow(false);
        //是否在归还到池中前进行检验；若设置为true，validationQuery参数必须设置为非空字符串
        dataSource.setTestOnReturn(false);
        //连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除；若设置为true，validationQuery参数必须设置为非空字符串
        dataSource.setTestWhileIdle(true);
        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        return dataSource;
    }

    @Bean(name = "org.tryimpl.framework.transaction.transactionmanager")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }
}
