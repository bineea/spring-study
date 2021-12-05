package org.tryImpl.framework.aop;

import java.lang.reflect.Method;

public interface ProceedingJoinPoint extends JoinPoint{

    Method getMethod();
}
