package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "reflect",
        name = "action-context-property"
)
public class ActionContextPropertyAction extends ActionContextAction {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM,
            required = true
    )
    private String propertyName;

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM
    )
    private boolean required;

    @Override
    public String call() throws Exception {
        return actionContext().getProperty(propertyName, required);
    }
}
