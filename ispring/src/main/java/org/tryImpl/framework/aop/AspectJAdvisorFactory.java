package org.tryImpl.framework.aop;

import java.util.List;

public interface AspectJAdvisorFactory {

    List<Advisor> getAdvisors(Class<?> clazz);
}
