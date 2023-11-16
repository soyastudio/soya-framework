package soya.framework.action.actions.jdbc;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "jdbc",
        name = "sql-executor"
)
public class DefaultJdbcAction extends JdbcAction<String> {

    @ActionParameter(
            type = ActionParameterType.INPUT
    )
    private String script;

    @Override
    public String call() throws Exception {

        return "";
    }
}