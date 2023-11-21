package soya.framework.action.actions.jdbc;

import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;
import soya.framework.context.ServiceLocatorSingleton;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

public abstract class DatabaseUtilAction<T> implements Callable<T> {

    @ActionParameter(type = ActionParameterType.PROPERTY)
    protected String dataSourceName;

    protected DataSource getDataSource() {
        ServiceLocator serviceLocator = ServiceLocatorSingleton.getInstance();
        DataSource dataSource = null;
        if (dataSourceName != null && !dataSourceName.isEmpty()) {
            try {
                dataSource = serviceLocator.getService(dataSourceName, DataSource.class);

            } catch (ServiceLocateException e) {

            }

            try {
                DataSourceFactory factory = serviceLocator.getService(DataSourceFactory.class);
                dataSource = factory.getDataSource(dataSourceName);

            } catch (ServiceLocateException e) {

            }

        } else {
            dataSource = serviceLocator.getService(DataSource.class);
        }

        return dataSource;
    }
}
