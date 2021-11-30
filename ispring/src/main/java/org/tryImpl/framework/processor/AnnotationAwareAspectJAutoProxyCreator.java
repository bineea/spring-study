package org.tryImpl.framework.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnotationAwareAspectJAutoProxyCreator extends AbstractAutoProxyCreator {

    private volatile List<String> aspectBeanNames;

    @Override
    protected List<Advisor> findCandidateAdvisors() {
        List<Advisor> advisors = this.buildAspectJAdvisors();
        return advisors;
    }

    private List<Advisor> buildAspectJAdvisors() {
        List<String> aspectNames = this.aspectBeanNames;

        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    aspectNames = new ArrayList<>();
                    //TODO 解析aspect
                }
            }
        }

        if (aspectNames.isEmpty()) {
            return Collections.emptyList();
        } else {
            //TODO 映射封装advisor
            return null;
        }
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> clazz, String beanName) {
        return new Object[0];
    }

    @Override
    protected Object createProxy(Class<?> clazz, String beanName, Object[] specificInterceptors) {
        return null;
    }
}
