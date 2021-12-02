package org.tryImpl.framework.aop;

import org.tryImpl.framework.annotation.Aspect;
import org.tryImpl.framework.context.ListableBeanFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactoryAspectJAdvisorsBuilder {

    private final ListableBeanFactory beanFactory;

    private final AspectJAdvisorFactory aspectJAdvisorFactory;

    private volatile List<String> aspectBeanNames;

    private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();

    public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory();
    }

    public List<Advisor> buildAspectJAdvisors() {
        List<String> aspectNames = this.aspectBeanNames;

        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    List<Advisor> advisors = new ArrayList<>();
                    aspectNames = new ArrayList<>();
                    //解析aspect
                    String[] beanNamesForType = beanFactory.getBeanNamesForType(Object.class);
                    for (String beanName : beanNamesForType) {
                        Class<?> beanType = beanFactory.getType(beanName);
                        if (beanType.isAnnotationPresent(Aspect.class)) {
                            List<Advisor> classAdvisors = aspectJAdvisorFactory.getAdvisors(beanType);
                            advisors.addAll(classAdvisors);
                        }
                    }
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

}
