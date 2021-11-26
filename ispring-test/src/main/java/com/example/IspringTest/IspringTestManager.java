package com.example.IspringTest;

import com.example.IspringTest.config.AppConfig;
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
    }
}
