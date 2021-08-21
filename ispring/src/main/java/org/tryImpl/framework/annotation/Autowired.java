package org.tryImpl.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解作用的目标：属性、方法、注解
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.ANNOTATION_TYPE})
//注解的生命周期：由JVM 加载，包含在类文件中，在运行时可以被获取到
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    //注解依赖项是否必须存在；默认true，必须存在
    boolean required() default true;
}
