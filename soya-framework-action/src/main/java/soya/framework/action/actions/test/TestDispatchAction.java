package soya.framework.action.actions.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.actions.dispatch.DispatchAction;
import soya.framework.action.actions.dispatch.DispatchActionDefinition;
import soya.framework.action.actions.dispatch.DispatchActionParameter;

@ActionDefinition(
        domain = "test",
        name = "dispatchActionTest",
        parameters = {
                @ActionParameterDefinition(name = "value",
                        type = ActionParameterType.WIRED_PROPERTY,
                        referredTo = "albertsons.workspace.home")
        }
)
@DispatchActionDefinition(uri = "class:soya.framework.action.actions.test.Utility?method=echo",
        parameters = {
                @DispatchActionParameter(type = String.class, implType = String.class, actionParameter = "value")
        }
)
public class TestDispatchAction extends DispatchAction<Object> {

}
