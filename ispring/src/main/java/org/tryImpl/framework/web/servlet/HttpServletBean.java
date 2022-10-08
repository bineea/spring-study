package org.tryImpl.framework.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class HttpServletBean extends HttpServlet {

    @Override
    public void init() throws ServletException {
        initServletBean();
    }

    protected void initServletBean() throws ServletException {
    }
}
