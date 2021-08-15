package org.tryImpl.service;

import org.tryImpl.framework.annotation.Autowired;
import org.tryImpl.framework.annotation.Component;

@Component
public class StartService {

    @Autowired
    private HelloService helloService;

    public void first2hello() {
        helloService.helloworld(1);
    }
}
