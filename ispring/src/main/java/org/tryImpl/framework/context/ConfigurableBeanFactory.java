package org.tryImpl.framework.context;

import org.tryImpl.framework.processor.BeanPostProcessor;

/**
 * Title: ConfigurableBeanFactory<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2021/11/26 10:28
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    void addBeanPostPorcessor(BeanPostProcessor beanPostProcessor);
}
