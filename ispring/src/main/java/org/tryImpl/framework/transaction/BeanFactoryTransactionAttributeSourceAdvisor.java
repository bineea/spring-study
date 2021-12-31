package org.tryImpl.framework.transaction;

import org.tryImpl.framework.aop.Advice;
import org.tryImpl.framework.aop.Pointcut;
import org.tryImpl.framework.aop.PointcutAdvisor;

public class BeanFactoryTransactionAttributeSourceAdvisor implements PointcutAdvisor {
    @Override
    public Pointcut getPointcut() {
        return null;
    }

    @Override
    public Advice getAdvice() {
        return null;
    }
}
