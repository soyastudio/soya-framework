package soya.framework.restruts.reflect;

import soya.framework.restruts.*;
import soya.framework.restruts.actions.ServiceDispatchAction;

@RestAction(
        id = "dispatch",
        path = "/restruts/dispatch/action-execute",
        method = HttpMethod.POST,
        parameters = {
                @RestActionParameter(name = "command", paramType = ParamType.PAYLOAD)
        },
        consumes = MediaType.TEXT_PLAIN,
        produces = MediaType.TEXT_PLAIN,
        tags = "Restruct",
        action = "soya.framework.action.ActionExecutor/execute(String)?0=command"
)
public class ActionExecuteCommand extends ServiceDispatchAction<String> {
    private String command;

    public String call() throws Exception {
        return super.call();
    }
}
