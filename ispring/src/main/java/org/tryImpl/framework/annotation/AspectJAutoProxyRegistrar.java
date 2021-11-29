package org.tryImpl.framework.annotation;

import org.tryImpl.framework.context.BeanDefinition;
import org.tryImpl.framework.context.BeanDefinitionRegistry;
import org.tryImpl.framework.context.ScopeEnum;
import org.tryImpl.framework.processor.AnnotationAwareAspectJAutoProxyCreator;

public class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String AUTO_PROXY_CREATOR_BEAN_NAME =
            "org.springframework.aop.config.internalAutoProxyCreator";

    @Override
    public void registerBeanDefinitions(BeanDefinitionRegistry beanDefinitionRegistry) {
        //实现beanPostProcessor注册
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(AUTO_PROXY_CREATOR_BEAN_NAME);
        beanDefinition.setScope(ScopeEnum.singleton);
        beanDefinition.setLazy(false);
        beanDefinition.setBeanClass(AnnotationAwareAspectJAutoProxyCreator.class);
        beanDefinitionRegistry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
    }
}
