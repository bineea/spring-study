package org.tryImpl.framework.web.servlet;

import org.tryImpl.framework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrameworkServlet extends HttpServletBean {

    @Override
    protected void initServletBean() throws ServletException {
        //TODO
        /**
         * 初始化application context
         * 触发refresh方法
         * 触发onRefresh方法
         */
    }

    protected void onRefresh(ApplicationContext context) {
        // For subclasses: do nothing by default.
    }


    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO
        try {
            doService(request, response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }
}
