package soya.framework.action.actions.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.actions.dispatch.Dispatch;
import soya.framework.action.actions.dispatch.DispatchAction;

@ActionDefinition(
        domain = "test",
        name = "dispatchActionTest",
        parameters = {
                @ActionParameterDefinition(name = "value",
                        type = ActionParameterType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
@Dispatch(uri = "bean:xyz?method=invoke")
public class TestDispatchAction extends DispatchAction<Object> {

    @Override
    public Object call() throws Exception {
        return getParameter("value");
    }
}
