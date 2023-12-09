package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.orchestration.AnnotatedDispatchAction;
import soya.framework.action.orchestration.DispatchDefinition;
import soya.framework.action.orchestration.ParameterMapping;

@ActionDefinition(
        domain = "test",
        name = "dispatchActionTest",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        type = String.class,
                        propertyType = ActionPropertyType.INPUT)
        }
)
@DispatchDefinition(uri = "action:text-utils://base64encode",
        parameterMappings = {
                @ParameterMapping(name = "text", mappingTo = "jsonpath()")
        }
)
public class TestDispatchAction extends AnnotatedDispatchAction<Object> {

}
