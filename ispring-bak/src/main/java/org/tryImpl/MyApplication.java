package org.tryImpl;

import org.tryImpl.config.AppConfig;
import org.tryImpl.framework.AnnotationConfigApplicationContext;
import org.tryImpl.service.StartService;

public class MyApplication {

    public static void main(String[] args) {
        //XML启动实例


        //注解启动实例
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);
        StartService startService = (StartService) annotationConfigApplicationContext.getBean("startservice");
        startService.first2hello();
    }
}
