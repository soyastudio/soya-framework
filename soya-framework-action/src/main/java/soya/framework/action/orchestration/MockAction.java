package soya.framework.action.orchestration;

import soya.framework.action.orchestration.annotation.MockActionDefinition;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public abstract class MockAction<T> extends AnnotatedDynaAction<T> {

    private static Logger logger = Logger.getLogger(MockAction.class.getName());

    public MockAction() {
        super();
        if(getClass().getAnnotation(MockActionDefinition.class) != null) {

        }
    }

    @Override
    public T call() throws Exception {
        logger.warning("");
        return getMockResult();
    }

    protected T getMockResult() {
        Method method = ReflectUtils.findMethod(getClass(), "call", new Class[0]);
        return (T) method.getDefaultValue();

    }

}
