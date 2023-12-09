package soya.framework.action.camel;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "test",
        name = "CamelFlow",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestCamelAction<String> extends CamelAction<String> {

}
