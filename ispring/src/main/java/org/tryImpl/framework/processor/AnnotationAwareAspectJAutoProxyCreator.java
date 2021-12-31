package org.tryImpl.framework.processor;

import org.tryImpl.framework.aop.Advisor;
import org.tryImpl.framework.aop.BeanFactoryAspectJAdvisorsBuilder;
import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AnnotationAwareAspectJAutoProxyCreator extends AbstractAutoProxyCreator {

    private BeanFactoryAspectJAdvisorsBuilder beanFactoryAspectJAdvisorsBuilder;

    @Override
    protected List<Advisor> findCandidateAdvisors() {
        List<Advisor> candidateAdvisors = super.findCandidateAdvisors();
        if (beanFactoryAspectJAdvisorsBuilder != null) {
            List<Advisor> advisors = beanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors();
            if (advisors != null && !advisors.isEmpty()) {
                //执行Collections.emptyList()，并不能直接操作add；因为Collections.emptyList()没有进行初始化操作
                if (candidateAdvisors == null || candidateAdvisors.isEmpty()) {
                    candidateAdvisors = new LinkedList();
                }
                candidateAdvisors.addAll(advisors);
            }
        }
        return candidateAdvisors;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new RuntimeException("AnnotationAwareAspectJAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        beanFactoryAspectJAdvisorsBuilder = new BeanFactoryAspectJAdvisorsBuilder((ConfigurableListableBeanFactory) beanFactory);
    }

}
