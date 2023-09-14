package soya.framework.container;

import java.lang.annotation.Annotation;
import java.util.Properties;
import java.util.Set;

public interface ContextInitializer<T extends Context> {
    ServiceLocator getServiceLocator();

    Properties getProperties();

    ComponentScanner getComponentScanner();

    ComponentFactory getComponentFactory();

    Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationType);

    T initialize();
}
