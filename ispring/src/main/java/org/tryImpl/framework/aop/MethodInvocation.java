package org.tryImpl.framework.aop;

import java.lang.reflect.Method;

public interface MethodInvocation extends JoinPoint {

    Method getMethod();
}
