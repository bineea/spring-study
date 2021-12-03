package org.tryImpl.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解作用的目标：方法
@Target({ElementType.METHOD})
//注解的生命周期：由JVM 加载，包含在类文件中，在运行时可以被获取到
@Retention(RetentionPolicy.RUNTIME)
public @interface Around {

    //切点表达式
    String value();
}
