package org.tryImpl.framework.aop;

import org.tryImpl.framework.annotation.Around;
import org.tryImpl.framework.context.ListableBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveAspectJAdvisorFactory implements AspectJAdvisorFactory, AspectJAdviceFactory {

    @Override
    public List<Advisor> getAdvisors(Class<?> clazz, ListableBeanFactory beanFactory, String beanName) {
        List<Advisor> advisors = new ArrayList<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Around.class)) {
                String pointcutExpression = method.getDeclaredAnnotation(Around.class).value();
                AspectJExpressionPointcut expressionPointcut = new AspectJExpressionPointcut(pointcutExpression);
                Advisor advisor = new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, method, this, beanFactory, beanName);
                advisors.add(advisor);
            }
        }
        return advisors;
    }

    @Override
    public Advice getAdvice(AspectJExpressionPointcut aspectJExpressionPointcut, Method method, ListableBeanFactory beanFactory, String beanName) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (Around.class.equals(annotation.annotationType())) {
                return new AspectJAroundAdvice(beanFactory, beanName, method);
            }
        }
        return null;
    }
}
