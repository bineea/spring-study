package org.tryImpl.service;

import org.tryImpl.framework.BeanNameAware;
import org.tryImpl.framework.annotation.Component;

@Component("helloService")
public class HelloService implements BeanNameAware {

    private String beanName;

    public void helloworld(int i) {
        System.out.println("beanName:"+getBeanName()+"\n"+"hello world:"+i);
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
