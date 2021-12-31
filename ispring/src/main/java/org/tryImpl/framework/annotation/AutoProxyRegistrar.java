package org.tryImpl.framework.annotation;

import org.tryImpl.framework.aop.AopConfigUtils;
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

        //暂时只支持处理AdviceMode.PROXY模式
        if (beanDefinitionRegistry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME) != null) {
            BeanDefinition apcDefinition = beanDefinitionRegistry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
            //比较后置处理器优先级（AOP）
            //通过AopConfigUtils的list静态维护AOP后置处理器的优先级
            if (!InfrastructureAdvisorAutoProxyCreator.class.getName().equals(apcDefinition.getBeanClass().getName())) {
                int currentPriority = AopConfigUtils.findPriorityForClass(apcDefinition.getBeanClass().getName());
                int requiredPriority = AopConfigUtils.findPriorityForClass(InfrastructureAdvisorAutoProxyCreator.class);
                if (currentPriority < requiredPriority) {
                    apcDefinition.setBeanClass(InfrastructureAdvisorAutoProxyCreator.class);
                }
            }
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
