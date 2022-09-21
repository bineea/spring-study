package org.tryImpl.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends FrameworkServlet {

    @Override
    public void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //TODO
        doDispatch(request, response);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;

        try {
            mappedHandler = this.getHandler(processedRequest);
        } catch (Exception exception) {

        }
    }

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        //TODO
        return null;
    }
}
