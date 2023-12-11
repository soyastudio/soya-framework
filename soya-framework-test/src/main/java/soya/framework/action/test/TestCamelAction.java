package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.camel.CamelRouteAction;
import soya.framework.action.camel.CamelRouteFlowBuilder;

@ActionDefinition(
        domain = "test",
        name = "CamelFlow",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestCamelAction extends CamelRouteAction<String> {

    public TestCamelAction() {
        super();
    }

    @Override
    protected void build(CamelRouteFlowBuilder builder) {
        builder.
                from("timer://foo?period=5000")
                .setBody().simple("Hello, world!")
                .to("log:mylogger");
    }
}
