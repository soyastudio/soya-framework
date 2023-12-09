package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "reflect",
        name = "action-context-resource"
)
public class ActionContextResourceAction extends ActionContextAction {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM,
            required = true
    )
    private String uri;


    @Override
    public String call() throws Exception {
        return actionContext().getResource(uri, String.class);
    }
}
