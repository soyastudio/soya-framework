package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "reflect",
        name = "action-context-service"
)
public class ActionContextServiceAction extends ActionContextAction {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM
    )
    private String name;

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM,
            required = true
    )
    private String type;

    @Override
    public String call() throws Exception {
        Object service = actionContext().getService(name, Class.forName(type));
        return service.getClass().getName();
    }
}
