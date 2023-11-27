package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "dispatch"
)
public class GenericDispatchAction implements Callable<Object> {

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY, required = true)
    private String uri;

    @ActionParameterDefinition(type = ActionParameterType.INPUT, required = true)
    private String input;

    @Override
    public Object call() throws Exception {
        return null;
    }
}
