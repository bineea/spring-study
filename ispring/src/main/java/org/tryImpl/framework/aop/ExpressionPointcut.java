package org.tryImpl.framework.aop;

/**
 * Title: ExpressionPointcut<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2021/12/3 17:32
 */
public interface ExpressionPointcut extends Pointcut {

    String getExpressPointcut();
}
