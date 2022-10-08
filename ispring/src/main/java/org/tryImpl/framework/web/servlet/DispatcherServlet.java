package org.tryImpl.framework.web.servlet;

import org.tryImpl.framework.context.ApplicationContext;
import org.tryImpl.framework.web.mvc.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DispatcherServlet extends FrameworkServlet {

    private List<HandlerMapping> handlerMappings;

    private List<HandlerAdapter> handlerAdapters;

    @Override
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    /**
     * Initialize the strategy objects that this servlet uses.
     * <p>May be overridden in subclasses in order to initialize further strategy objects.
     */
    protected void initStrategies(ApplicationContext context) {
        //TODO
//        initHandlerMappings(context);
//        initHandlerAdapters(context);
//        initViewResolvers(context);
    }

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
            HandlerAdapter ha = getHandleAdapter(mappedHandler);
            ha.handle(request, response, mappedHandler);
        } catch (Exception exception) {

        }
    }

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        //TODO
        return null;
    }

    protected HandlerAdapter getHandleAdapter(Object handler) {
        //TODO
        return null;
    }
}
