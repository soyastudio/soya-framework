package soya.framework.action.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.dsl.ActionParser;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "dsl",
        name = "action-match"
)
public class ActionMatchAction implements Callable<String> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT,
            required = true)
    private String expression;

    @Override
    public String call() throws Exception {
        return ActionParser.parse(expression).toString();
    }
}
