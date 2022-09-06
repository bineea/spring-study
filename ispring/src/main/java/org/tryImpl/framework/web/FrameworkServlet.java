package org.tryImpl.framework.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrameworkServlet extends HttpServlet {

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
