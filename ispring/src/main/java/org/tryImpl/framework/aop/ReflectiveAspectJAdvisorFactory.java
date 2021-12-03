package org.tryImpl.framework.aop;

import org.tryImpl.framework.annotation.Around;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveAspectJAdvisorFactory implements AspectJAdvisorFactory {

    @Override
    public List<Advisor> getAdvisors(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Around.class)) {
                String pointcutValue = method.getAnnotation(Around.class).value();

            }
        }

        return null;
    }
}
