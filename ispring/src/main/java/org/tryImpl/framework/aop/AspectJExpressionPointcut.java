package org.tryImpl.framework.aop;

import org.tryImpl.framework.annotation.Around;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class AspectJExpressionPointcut implements ExpressionPointcut, ClassFilter, MethodMatcher {

    private String expression;

    private PointcutType pointcutType;

    private volatile String pointExpression;

    private static final Set<PointcutType> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutType.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutType.ANNOTATION);
    }

    public AspectJExpressionPointcut(String expression) {
        this.expression = expression;
        PointcutType pointcutType = null;
        for (PointcutType pt : SUPPORTED_PRIMITIVES) {
            if (this.expression.trim().startsWith(pt.getPointcutPrimitive())) {
                pointcutType = pt;
                break;
            }
        }
        if (pointcutType == null) {
            throw new RuntimeException("Unable to parse pointcut expression");
        }
        this.pointcutType = pointcutType;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        this.obtainPointcutExpression();
        for (Method method : clazz.getDeclaredMethods()) {
            if (match(method)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(Method method) {
        this.obtainPointcutExpression();
        if (match(method)) {
            return true;
        }
        return false;
    }

    private void obtainPointcutExpression() {
        if (pointExpression == null || pointExpression.length() <= 0) {
            synchronized (this) {
                if (pointExpression == null || pointExpression.length() <= 0) {
                    this.pointExpression = this.expression.replaceAll("\\s","");
                }
            }
        }
    }

    public enum PointcutType {
        EXECUTION("execution"){},
        ANNOTATION("@annotation"){},
        ;

        private String pointcutPrimitive;
        PointcutType(String pointcutPrimitive) {
            this.pointcutPrimitive = pointcutPrimitive;
        }

        public String getPointcutPrimitive() {
            return this.pointcutPrimitive;
        }
    }

    private boolean match(Method method) {
        if (PointcutType.EXECUTION.equals(pointcutType)) {
            StringBuilder pointcutExpBuilder = new StringBuilder();
            pointcutExpBuilder.append(pointcutType.getPointcutPrimitive());
            pointcutExpBuilder.append("(");
            pointcutExpBuilder.append(method.getDeclaringClass().getName());
            pointcutExpBuilder.append("(");
            Type[] parameterTypes = method.getGenericParameterTypes();
            for (int i=0; i<parameterTypes.length; i++) {
                pointcutExpBuilder.append(parameterTypes[i].getTypeName());
                if (i < parameterTypes.length -1 ) {
                    pointcutExpBuilder.append(",");
                }
            }
            pointcutExpBuilder.append(")");
            pointcutExpBuilder.append(")");
            if (pointcutExpBuilder.toString().equals(pointExpression)) {
                return true;
            }
        } else if (PointcutType.ANNOTATION.equals(pointcutType)) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                StringBuilder pointcutExpBuilder = new StringBuilder();
                pointcutExpBuilder.append(pointcutType.getPointcutPrimitive());
                pointcutExpBuilder.append("(");
                pointcutExpBuilder.append(annotation.getClass().getName());
                pointcutExpBuilder.append(")");
                if (pointcutExpBuilder.toString().equals(pointExpression)) {
                    return true;
                }
            }
        }
        return false;
    }
}
