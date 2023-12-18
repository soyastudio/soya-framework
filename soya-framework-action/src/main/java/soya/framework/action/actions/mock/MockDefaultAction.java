package soya.framework.action.actions.mock;


import soya.framework.action.ActionDefinition;
import soya.framework.commons.util.DefaultUtils;

@ActionDefinition(
        domain = "mock",
        name = "default"
)
public class MockDefaultAction extends AbstractMockAction {

    @Override
    protected Object getMockResult(Class<?> expectedType) {
        return DefaultUtils.getDefaultValue(expectedType);
    }
}
