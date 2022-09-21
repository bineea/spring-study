package org.tryImpl.framework.web.context;

import org.tryImpl.framework.context.AbstractApplicationContext;
import org.tryImpl.framework.context.ConfigurableListableBeanFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext {

    private static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = "WebApplicationContext.ROOT";
    private static final String SCOPE_REQUEST = "request";
    private static final String SCOPE_SESSION = "session";
    private static final String SCOPE_APPLICATION = "application";
    private static final String SERVLET_CONTEXT_BEAN_NAME = "servletContext";
    private static final String CONTEXT_PARAMETERS_BEAN_NAME = "contextParameters";
    private static final String CONTEXT_ATTRIBUTES_BEAN_NAME = "contextAttributes";

    private ServletContext servletContext;
    private ServletConfig servletConfig;

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return null;
    }
}
