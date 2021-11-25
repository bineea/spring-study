package org.tryImpl.framework.context;

/**
 * Title: ListableBeanFactory<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2021/11/25 22:33
 */
public interface ListableBeanFactory extends BeanFactory {

    String[] getBeanNamesForType(Class<?> type);
}
