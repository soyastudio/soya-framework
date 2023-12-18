package soya.framework.action.actions.mock;

import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.util.concurrent.Callable;

public abstract class StrategyMockAction implements Callable<Object> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    protected String strategy;

    protected Object getMockResult(Class<?> expectedType) {
        return null;
    }
}
