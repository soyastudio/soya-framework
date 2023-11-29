package soya.framework.action.actions.jdbc;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "jdbc",
        name = "sql-executor"
)
public class DefaultJdbcAction extends JdbcAction<String> {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.INPUT
    )
    private String script;

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.WIRED_VALUE,
            referredTo = "true"
    )
    private boolean autoCommit;

    @Override
    public String call() throws Exception {

        return "";
    }
}
