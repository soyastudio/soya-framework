package soya.framework.spring;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class ComponentTest {
    
    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testXYZ() {
        System.out.println("========================= XYZ: " + applicationContext);
    }
}
