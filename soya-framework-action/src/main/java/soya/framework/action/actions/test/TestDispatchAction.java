package soya.framework.action.actions.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.actions.dispatch.DispatchAction;
import soya.framework.action.actions.dispatch.DispatchActionDefinition;
import soya.framework.action.actions.dispatch.PropertyMapping;

@ActionDefinition(
        domain = "test",
        name = "dispatchActionTest",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        type = String.class,
                        propertyType = ActionPropertyType.WIRED_RESOURCE,
                        referredTo = "classpath:banner.txt")
        }
)
@DispatchActionDefinition(uri = "bean:com.albertsons.workspace.configuration.EchoService?method=echo",
        propertyMappings = {
                @PropertyMapping(type = String.class, actionProperty = "value")
        }
)
public class TestDispatchAction extends DispatchAction<Object> {

}
