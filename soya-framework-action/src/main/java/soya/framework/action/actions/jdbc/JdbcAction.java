package soya.framework.action.actions.jdbc;

import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

public abstract class JdbcAction<T> implements Callable<T> {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.ATTRIBUTE
    )
    private String dataSourceName;

    protected DataSource getDataSource() {
        return null;
    }

}
