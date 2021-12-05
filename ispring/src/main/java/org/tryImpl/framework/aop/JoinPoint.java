package org.tryImpl.framework.aop;

public interface JoinPoint {

    Object proceed() throws Throwable;
}
