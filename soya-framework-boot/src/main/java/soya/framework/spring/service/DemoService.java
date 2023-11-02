package soya.framework.spring.service;

import org.springframework.stereotype.Service;

@Service()
public class DemoService {
    public String test(String a, String b) {
        return a + " " + b;
    }
}
