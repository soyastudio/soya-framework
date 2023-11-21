package soya.framework.action.actions.jdbc;

import javax.sql.DataSource;

public interface DataSourceFactory {
    DataSource getDataSource(String name);

}
