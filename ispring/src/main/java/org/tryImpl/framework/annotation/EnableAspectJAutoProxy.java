package org.tryImpl.framework.annotation;

import org.tryImpl.framework.processor.AnnotationAwareAspectJAutoProxyCreator;

import java.lang.annotation.*;

//注解作用的目标：类
@Target({ElementType.TYPE})
//注解的生命周期：由JVM 加载，包含在类文件中，在运行时可以被获取到
@Retention(RetentionPolicy.RUNTIME)

//FIXME 此处需要注入aop的解析类
@Import(AnnotationAwareAspectJAutoProxyCreator.class)
public @interface EnableAspectJAutoProxy {
}
