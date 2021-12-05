package com.example.IspringTest.config;

import org.tryImpl.framework.annotation.ComponentScan;
import org.tryImpl.framework.annotation.Configuration;
import org.tryImpl.framework.annotation.EnableAspectJAutoProxy;

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
@ComponentScan({"com.example.IspringTest"})
public class AppConfig {
}
