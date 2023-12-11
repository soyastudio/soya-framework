package soya.framework.action.camel;

import org.apache.camel.CamelContext;
import soya.framework.action.orchestration.Session;
import soya.framework.action.orchestration.Workflow;
import soya.framework.context.ServiceLocatorSingleton;

public class CamelRouteFlow implements Workflow {
    @Override
    public Object execute(Session session) throws Exception {
        CamelContext context = ServiceLocatorSingleton.getInstance().getService(CamelContext.class);

        return "Camel Flow";
    }
}
