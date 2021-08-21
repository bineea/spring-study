package org.tryImpl.framework;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    private String contextConfigLocation;

    private Class<?> contextClass;

    private ContextLoaderListener() {}

    public ContextLoaderListener(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    public ContextLoaderListener(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public void contextInitialized(ServletContextEvent sce) {

    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
