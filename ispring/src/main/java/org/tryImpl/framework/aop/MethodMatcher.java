package org.tryImpl.framework.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean matches(Method method);
}
