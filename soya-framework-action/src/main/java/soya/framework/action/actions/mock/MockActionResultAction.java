package soya.framework.action.actions.mock;

import soya.framework.action.*;
import soya.framework.commons.util.DefaultUtils;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "mock",
        name = "action-result"
)
public class MockActionResultAction implements Callable<Object> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM,
            required = true)
    private String actionName;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    private String resource;

    @Override
    public Object call() throws Exception {
        Class<?> resultType = ActionClass.forName(ActionName.fromURI(actionName)).getResultType();
        if (resource == null) {
            return DefaultUtils.getDefaultValue(resultType);

        } else {
            return ServiceLocatorSingleton
                    .getInstance()
                    .getService(ActionContext.class)
                    .getResource(resource, resultType);

        }
    }
}
