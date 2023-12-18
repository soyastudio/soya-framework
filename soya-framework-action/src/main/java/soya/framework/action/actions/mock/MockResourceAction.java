package soya.framework.action.actions.mock;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.context.ServiceLocatorSingleton;

@ActionDefinition(
        domain = "mock",
        name = "resource"
)
public class MockResourceAction extends AbstractMockAction {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM,
            required = true)
    private String uri;

    @Override
    protected Object getMockResult(Class<?> expectedType) {
        return ServiceLocatorSingleton.getInstance().getService(ActionContext.class).getResource(uri, expectedType);
    }
}
