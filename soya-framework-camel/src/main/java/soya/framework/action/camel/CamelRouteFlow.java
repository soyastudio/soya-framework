package soya.framework.action.camel;

import org.apache.camel.*;
import org.apache.camel.component.direct.DirectComponent;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import soya.framework.action.orchestration.Session;
import soya.framework.action.orchestration.Workflow;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.Map;

public class CamelRouteFlow implements Workflow {

    @Override
    public Object execute(Session session) throws Exception {
        DirectEndpoint directEndpoint = new DirectEndpoint(session.getActionName().toString(), new DirectComponent());

        CamelContext context = ServiceLocatorSingleton.getInstance().getService(CamelContext.class);
        DefaultProducerTemplate template = new DefaultProducerTemplate(context, directEndpoint);
        template.start();
        Exchange exchange = new DefaultExchange(context);
        template.send(exchange);

        return "Camel Flow";
    }
}
