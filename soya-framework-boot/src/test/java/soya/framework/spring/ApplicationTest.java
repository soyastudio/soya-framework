package soya.framework.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import soya.framework.context.ServiceLocatorSingleton;

@SpringBootTest
public class ApplicationTest {

    @Test
    public void testServiceLocator() {
        Assertions.assertNotNull(ServiceLocatorSingleton.getInstance());
    }
}
