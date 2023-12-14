package soya.framework.action.orchestration;

import soya.framework.action.orchestration.annotation.MockActionDefinition;

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
        return null;

    }


}
