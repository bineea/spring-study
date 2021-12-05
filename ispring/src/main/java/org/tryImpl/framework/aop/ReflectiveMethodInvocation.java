package org.tryImpl.framework.aop;

import org.tryImpl.framework.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation  {

    private Object proxy;
    private Object target;
    private Method method;
    private Object[] args;
    private List<MethodInterceptor> methodInterceptorList;
    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodInterceptorList = methodInterceptorList;
    }

    protected Object invokeJoinpoint() throws Throwable {
        return getMethod().invoke(this.target, this.args);
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.methodInterceptorList.size() - 1) {
            return this.invokeJoinpoint();
        }
        MethodInterceptor methodInterceptor = methodInterceptorList.get(++this.currentInterceptorIndex);
        return methodInterceptor.invoke(this);
    }
}
