package org.tryImpl.framework.transaction;

import org.tryImpl.framework.context.BeanFactory;
import org.tryImpl.framework.context.BeanFactoryAware;

public abstract class TransactionAspectSupport implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

}
