package org.tryImpl.framework.web.servlet;

public class ModelAndView {

    private Object view;
    private ModelMap modelMap;
    private int httpStatus;

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public ModelMap getModelMap() {
        return modelMap;
    }

    public void setModelMap(ModelMap modelMap) {
        this.modelMap = modelMap;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
