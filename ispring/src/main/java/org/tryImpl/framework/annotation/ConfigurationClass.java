package org.tryImpl.framework.annotation;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConfigurationClass {

    private String beanName;
    private Class<?> clazz;
    private Set<ConfigurationClass> importedBy = new LinkedHashSet<>(1);

    public ConfigurationClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ConfigurationClass(String beanName, Class<?> clazz) {
        this.beanName = beanName;
        this.clazz = clazz;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Set<ConfigurationClass> getImportedBy() {
        return importedBy;
    }

    public void setImportedBy(Set<ConfigurationClass> importedBy) {
        this.importedBy = importedBy;
    }
}
