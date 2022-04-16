package com.example.IspringTest.config;

import com.example.IspringTest.service.CreateBeanByMethod;
import com.example.IspringTest.service.HelloworldServiceImpl;
import org.tryImpl.framework.annotation.*;

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
}
