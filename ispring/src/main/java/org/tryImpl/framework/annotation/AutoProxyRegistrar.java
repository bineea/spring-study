package org.tryImpl.framework.annotation;

import org.tryImpl.framework.context.BeanDefinition;
import org.tryImpl.framework.context.BeanDefinitionRegistry;
import org.tryImpl.framework.context.ScopeEnum;
import org.tryImpl.framework.processor.AnnotationAwareAspectJAutoProxyCreator;
import org.tryImpl.framework.processor.InfrastructureAdvisorAutoProxyCreator;

public class AutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

    public static final String AUTO_PROXY_CREATOR_BEAN_NAME =
            "org.springframework.aop.config.internalAutoProxyCreator";

    @Override
    public void registerBeanDefinitions(BeanDefinitionRegistry beanDefinitionRegistry) {

        //FIXME 暂时默认处理AdviceMode.PROXY模式
        if (beanDefinitionRegistry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME) != null) {
            BeanDefinition apcDefinition = beanDefinitionRegistry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
            //TODO 比较后置处理器优先级（AOP）
//            if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
//                int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
//                int requiredPriority = findPriorityForClass(cls);
//                if (currentPriority < requiredPriority) {
//                    apcDefinition.setBeanClassName(cls.getName());
//                }
//            }
            return;
        }

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanName(AUTO_PROXY_CREATOR_BEAN_NAME);
        beanDefinition.setScope(ScopeEnum.singleton);
        beanDefinition.setLazy(false);
        beanDefinition.setBeanClass(AnnotationAwareAspectJAutoProxyCreator.class);
        beanDefinitionRegistry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
    }
}
