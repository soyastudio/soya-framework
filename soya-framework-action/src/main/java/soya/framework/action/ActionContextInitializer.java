package soya.framework.action;

import java.lang.annotation.Annotation;
import java.util.Properties;
import java.util.Set;

public interface ActionContextInitializer {
    ServiceLocator getServiceLocator();

    Properties getProperties();

    Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationType);
}
