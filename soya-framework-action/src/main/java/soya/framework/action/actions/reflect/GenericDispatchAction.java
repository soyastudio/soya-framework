package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "dispatch"
)
public class GenericDispatchAction implements Callable<Object> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM, required = true)
    private String uri;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT, required = true)
    private String input;

    @Override
    public Object call() throws Exception {
        return null;
    }
}
