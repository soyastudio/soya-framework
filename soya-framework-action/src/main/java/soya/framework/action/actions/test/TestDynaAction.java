package soya.framework.action.actions.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.actions.AnnotatedDynaAction;

@ActionDefinition(
        domain = "test",
        name = "dynaActionTest",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestDynaAction extends AnnotatedDynaAction {

    @Override
    public Object call() throws Exception {
        return getParameter("value");
    }
}
