package soya.framework.action.camel.admin;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "camel-admin",
        name = "remove-route"
)
public class RemoveRouteAction extends CamelAdminAction<String> {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM,
            required = true
    )
    private String id;

    @Override
    public String call() throws Exception {
        getCamelContext().removeRoute(id);

        return "";
    }
}
