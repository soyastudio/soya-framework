package soya.framework.action.actions.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.actions.AnnotatedDynaAction;

@ActionDefinition(
        domain = "test",
        name = "dynaActionTest",
        parameters = {
                @ActionParameterDefinition(name = "value",
                        parameterType = ActionParameterType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestDynaAction extends AnnotatedDynaAction {

    @Override
    public Object call() throws Exception {
        return getParameter("value");
    }
}
