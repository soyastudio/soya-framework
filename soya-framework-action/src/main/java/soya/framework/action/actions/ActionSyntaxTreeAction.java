package soya.framework.action.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.dsl.ActionSyntaxTree;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "dsl",
        name = "action-syntax-tree"
)
public class ActionSyntaxTreeAction implements Callable<ActionSyntaxTree> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT,
            required = true)
    private String expression;

    @Override
    public ActionSyntaxTree call() throws Exception {
        return ActionSyntaxTree.parse(expression);
    }
}
