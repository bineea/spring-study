package org.tryImpl.framework.aop;

import org.tryImpl.framework.annotation.Aspect;
import org.tryImpl.framework.context.ListableBeanFactory;

import java.util.*;
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
                    List<Advisor> advisors = new LinkedList<>();
                    aspectNames = new LinkedList<>();
                    //解析aspect
                    String[] beanNamesForType = beanFactory.getBeanNamesForType(Object.class);
                    for (String beanName : beanNamesForType) {
                        Class<?> beanType = beanFactory.getType(beanName);
                        if (beanType != null && beanType.isAnnotationPresent(Aspect.class)) {
                            List<Advisor> classAdvisors = aspectJAdvisorFactory.getAdvisors(beanType, beanFactory, beanName);
                            if (classAdvisors != null && !classAdvisors.isEmpty()) {
                                aspectNames.add(beanName);
                                advisorsCache.put(beanName, classAdvisors);
                                advisors.addAll(classAdvisors);
                            }
                        }
                    }
                }
            }
        }

        if (aspectNames.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Advisor> advisors = new ArrayList<>();
            for (String aspectName : aspectNames) {
                advisors.addAll(advisorsCache.get(aspectName));
            }
            return advisors;
        }
    }

}
