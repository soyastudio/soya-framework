package soya.framework.action.actions.jdbc;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

public abstract class JdbcAction<T> implements Callable<T> {

    @ActionParameterDefinition(
            parameterType = ActionParameterType.ATTRIBUTE
    )
    private String dataSourceName;

    protected DataSource getDataSource() {
        return null;
    }

}
