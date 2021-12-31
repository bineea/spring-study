package org.tryImpl.framework.aop;

import org.tryImpl.framework.context.ConfigurableListableBeanFactory;
import org.tryImpl.framework.context.ListableBeanFactory;

import java.util.LinkedList;
import java.util.List;

public class BeanFactoryAdvisorRetrievalHelper {

    private final ConfigurableListableBeanFactory beanFactory;

    private volatile String[] cachedAdvisorBeanNames;

    public BeanFactoryAdvisorRetrievalHelper (ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public List<Advisor> findAdvisorBeans() {
        String[] advisorNames = cachedAdvisorBeanNames;
        if (advisorNames == null) {
            synchronized (this) {
                if (advisorNames == null) {
                    advisorNames = beanFactory.getBeanNamesForType(Advisor.class);
                    cachedAdvisorBeanNames = advisorNames;
                }
            }
        }

        if (advisorNames == null || advisorNames.length == 0) {
            return new LinkedList();
        } else {
            List<Advisor> advisors = new LinkedList();
            for (String advisorName : advisorNames) {
                Advisor advisor = (Advisor) beanFactory.getBean(advisorName);
                advisors.add(advisor);
            }
            return advisors;
        }
    }
}
