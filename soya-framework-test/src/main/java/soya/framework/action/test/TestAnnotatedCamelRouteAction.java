package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.camel.AnnotatedCamelRouteAction;
import soya.framework.action.camel.CamelRouteDefinition;
import soya.framework.action.orchestration.annotation.TaskDefinition;

@ActionDefinition(
        domain = "test",
        name = "AnnotatedCamelFlow",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
@CamelRouteDefinition(
        from = @TaskDefinition(
                uri = "timer://foo?period=5000"
        ),
        to = {
                @TaskDefinition(
                        uri = "log:mylogger"
                )
        }
)
public class TestAnnotatedCamelRouteAction extends AnnotatedCamelRouteAction<String> {
}
