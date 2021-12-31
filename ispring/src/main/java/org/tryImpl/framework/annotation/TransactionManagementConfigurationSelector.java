package org.tryImpl.framework.annotation;

import java.lang.annotation.Annotation;

public class TransactionManagementConfigurationSelector implements ImportSelector {

    // spring是通过继承抽象类，并通过抽象类定义泛型的形式，确定每个ImportSelector实现类对应的注解class
    private static final Class<?> ANNOTATION_CLASS = EnableTransactionManagement.class;

    @Override
    public String[] selectImports(Annotation[] annotations) {
        AdviceMode adviceMode = null;
        //获取mode属性值
        for (Annotation annotation : annotations) {
            if (ANNOTATION_CLASS.isAssignableFrom(annotation.annotationType())) {
                adviceMode = ((EnableTransactionManagement) annotation).mode();
            }
        }
        if (adviceMode != null && adviceMode != AdviceMode.PROXY) {
            throw new RuntimeException("暂时不支持“PROXY”模式外的其他模式");
        }
        return new String[] {
                AutoProxyRegistrar.class.getName(),
                ProxyTransactionManagementConfiguration.class.getName()
        };
    }
}
