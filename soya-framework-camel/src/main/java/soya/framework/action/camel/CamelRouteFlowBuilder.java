package soya.framework.action.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import soya.framework.action.orchestration.Workflow;
import soya.framework.action.orchestration.WorkflowBuilder;
import soya.framework.context.ServiceLocatorSingleton;

public class CamelRouteFlowBuilder extends RouteBuilder implements WorkflowBuilder {

    private final String id;
    public CamelRouteFlowBuilder(String id) {
        super(ServiceLocatorSingleton.getInstance().getService(CamelContext.class));
        this.id = id;
    }

    @Override
    public void configure() throws Exception {

    }

    @Override
    public Workflow create() {
        try {
            addRoutesToCamelContext(getContext());
            return new CamelRouteFlow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RouteDefinition from(String uri) {
        RouteDefinition definition = super.from(uri);
        definition.setId(id);
        return definition;
    }
}
