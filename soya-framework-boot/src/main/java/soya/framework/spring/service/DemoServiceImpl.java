package soya.framework.spring.service;

import org.springframework.stereotype.Service;

@Service("demo")
public class DemoServiceImpl implements DemoService{
    public String test(String a, String b) {
        return a + " " + b;
    }
}
