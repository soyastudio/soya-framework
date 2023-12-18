package soya.framework.action.actions.mock;

import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.util.concurrent.Callable;

public abstract class AbstractMockAction implements Callable<Object> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM,
            required = true)
    private Class<?> expectedType;

    @Override
    public Object call() throws Exception {
        return getMockResult(expectedType);
    }

    protected abstract Object getMockResult(Class<?> expectedType);
}
