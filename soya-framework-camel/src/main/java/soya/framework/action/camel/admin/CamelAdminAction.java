package soya.framework.action.camel.admin;

import org.apache.camel.CamelContext;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.concurrent.Callable;

public abstract class CamelAdminAction<T> implements Callable<T> {

    protected CamelContext getCamelContext() {
        return ServiceLocatorSingleton.getInstance().getService(CamelContext.class);
    }
}
