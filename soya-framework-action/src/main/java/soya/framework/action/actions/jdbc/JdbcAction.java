package soya.framework.action.actions.jdbc;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

public abstract class JdbcAction<T> implements Callable<T> {


    protected DataSource getDataSource() {
        return null;
    }

}
