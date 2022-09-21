package org.tryImpl.framework.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener extends ContextLoader implements ServletContextListener {

    private String contextConfigLocation;

    private Class<?> contextClass;

    private ContextLoaderListener() {}

    public ContextLoaderListener(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public ContextLoaderListener(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.initWebApplicationContext(event.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
