package com.example.IspringTest;

import com.example.IspringTest.config.AppConfig;
import com.example.IspringTest.dao.SimpleTestDao;
import com.example.IspringTest.service.CreateBeanByMethod;
import com.example.IspringTest.service.HelloworldService;
import com.example.IspringTest.service.TransactionalService;
import org.tryImpl.framework.context.AnnotationConfigApplicationContext;

/**
 * Title: IspringTestManager<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2021/11/26 16:32
 */
public class IspringTestManager {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println(annotationConfigApplicationContext);
        HelloworldService helloworldService = (HelloworldService) annotationConfigApplicationContext.getBean("helloworldServiceImpl");
        System.out.println(helloworldService);
        System.out.println(helloworldService.sayHelloworld());

        CreateBeanByMethod createBeanByMethod = (CreateBeanByMethod) annotationConfigApplicationContext.getBean("createBeanByMethod");
        System.out.println(createBeanByMethod);
        System.out.println(createBeanByMethod.doSomething());

        TransactionalService transactionalService = (TransactionalService) annotationConfigApplicationContext.getBean("transactionalServiceImpl");
        System.out.println(transactionalService);
        transactionalService.tryTransactionalOperate();

        SimpleTestDao simpleTestDao = (SimpleTestDao) annotationConfigApplicationContext.getBean("simpleTestDaoImpl");
        System.out.println(simpleTestDao);

    }
}
