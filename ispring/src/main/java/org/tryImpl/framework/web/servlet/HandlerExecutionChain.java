package org.tryImpl.framework.web.servlet;

import java.util.List;

public class HandlerExecutionChain {

    private final Object handler;
    private final List<HandlerInterceptor> interceptorList;
    private int interceptorIndex;

    public HandlerExecutionChain(Object handler, List<HandlerInterceptor> interceptorList) {
        this.handler = handler;
        this.interceptorList = interceptorList;
    }
}
