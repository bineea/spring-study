package org.tryImpl.framework.aop;

import org.tryImpl.framework.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdkDynamicAopProxy implements InvocationHandler {

    private List<Advisor> advisors;
    private Object targetBeanObj;
    private Map<Method, List<MethodInterceptor>> methodCache;

    public JdkDynamicAopProxy(List<Advisor> advisors, Object targetBeanObj) {
        this.advisors = advisors;
        this.targetBeanObj = targetBeanObj;
        this.methodCache = new ConcurrentHashMap<>();
    }

    public Object getProxy(ClassLoader classLoader, Class<?>[] interfaces) {
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object retVal;
        List<MethodInterceptor> chain = getInterceptorsAndDynamicInterceptionAdvice(method);
        if (chain.isEmpty()) {
            retVal = method.invoke(this.targetBeanObj, args);
        } else {
            ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(proxy, this.targetBeanObj, method, args, chain);
            retVal = invocation.proceed();
        }
        return retVal;
    }

    private List<MethodInterceptor> getInterceptorsAndDynamicInterceptionAdvice(Method method) {
        List<MethodInterceptor> methodInterceptors = methodCache.get(method);
        if (methodInterceptors == null) {
            methodInterceptors = new ArrayList<>();
            for (Advisor advisor : this.advisors) {
                if (advisor instanceof PointcutAdvisor) {
                    if (((PointcutAdvisor) advisor).getPointcut().getMethodMatcher().matches(method)) {
                        if (advisor.getAdvice() instanceof MethodInterceptor) {
                            methodInterceptors.add((MethodInterceptor) advisor.getAdvice());
                        }
                    }
                }
            }
            if (!methodInterceptors.isEmpty()) {
                methodCache.put(method, methodInterceptors);
            }
        }
        return methodInterceptors;
    }

}
