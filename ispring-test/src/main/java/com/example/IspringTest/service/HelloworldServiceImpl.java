package com.example.IspringTest.service;

import org.tryImpl.framework.annotation.Component;

@Component
public class HelloworldServiceImpl implements HelloworldService {

    @Override
    public String sayHelloworld() {
        return "^_^hello world^_^";
    }
}
