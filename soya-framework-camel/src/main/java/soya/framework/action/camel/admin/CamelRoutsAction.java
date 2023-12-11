package soya.framework.action.camel.admin;

import org.apache.camel.CamelContext;
import org.apache.camel.NamedRoute;
import org.apache.camel.Route;
import soya.framework.action.ActionDefinition;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.List;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "camel-admin",
        name = "Routes"
)
public class CamelRoutsAction implements Callable<String[]> {
    @Override
    public String[] call() throws Exception {
        CamelContext ctx = camelContext();
        List<Route> routes = camelContext().getRoutes();
        routes.forEach(e -> {
            System.out.println("================= " + e.isAutoStartup());

        });


        return new String[0];
    }

    protected CamelContext camelContext() {
        return ServiceLocatorSingleton.getInstance().getService(CamelContext.class);
    }
}
