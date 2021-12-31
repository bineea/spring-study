package org.tryImpl.framework.aop;

import org.tryImpl.framework.processor.AnnotationAwareAspectJAutoProxyCreator;
import org.tryImpl.framework.processor.InfrastructureAdvisorAutoProxyCreator;

import java.util.ArrayList;
import java.util.List;

public class AopConfigUtils {

    /**
     * Stores the auto proxy creator classes in escalation order.
     */
    private static final List<Class<?>> APC_PRIORITY_LIST = new ArrayList<>(3);

    static {
        // Set up the escalation list...
        APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);
        APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);
    }

    public static int findPriorityForClass(Class<?> clazz) {
        return APC_PRIORITY_LIST.indexOf(clazz);
    }

    public static int findPriorityForClass(String className) {
        for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
            Class<?> clazz = APC_PRIORITY_LIST.get(i);
            if (clazz.getName().equals(className)) {
                return i;
            }
        }
        throw new IllegalArgumentException(
                "Class name [" + className + "] is not a known auto-proxy creator class");
    }
}
