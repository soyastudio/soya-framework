package soya.framework.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.resource.ClasspathResource;

import java.net.URI;
import java.net.URISyntaxException;

public class ServiceLocatorTest {

    @Test
    public void testSingleton() {


    }

    @Test
    public void testProperty() {
        DefaultServiceLocator.addProperty("soya.framework.env.hello", "Hello World");
        Assertions.assertEquals("Hello World", ServiceLocatorSingleton.getInstance().getProperty("soya.framework.env.hello"));
        Assertions.assertThrowsExactly(ServiceLocateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ServiceLocatorSingleton.getInstance().getProperty("soya.framework.env.world");
            }
        });
    }

    @Test
    public void testResource() {
        DefaultServiceLocator.addResourceLoader("classpath", ClasspathResource.loader());

        try {
            Resource resource = ServiceLocatorSingleton.getInstance().getResource(new URI("classpath:banner.txt"));
            Assertions.assertNotNull(resource);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
