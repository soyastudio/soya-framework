package soya.framework.action.actions.jdbc;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "jdbc",
        name = "sql-executor"
)
public class DefaultJdbcAction extends JdbcAction<String> {

    @ActionParameterDefinition(
            type = ActionParameterType.INPUT
    )
    private String script;

    @ActionParameterDefinition(
            type = ActionParameterType.WIRED_VALUE,
            referredTo = "true"
    )
    private boolean autoCommit;

    @Override
    public String call() throws Exception {

        return "";
    }
}
