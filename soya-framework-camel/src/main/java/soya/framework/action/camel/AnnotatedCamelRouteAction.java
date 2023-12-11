package soya.framework.action.camel;

import org.apache.camel.model.RouteDefinition;
import soya.framework.action.orchestration.annotation.TaskDefinition;

import java.util.Arrays;

public abstract class AnnotatedCamelRouteAction<T> extends CamelRouteAction<T> {
    @Override
    protected void build(CamelRouteFlowBuilder builder) {
        CamelRouteDefinition definition = getClass().getAnnotation(CamelRouteDefinition.class);
        if (definition == null) {
            throw new IllegalArgumentException("Action is not annotated as 'CamelRouteDefinition'.");
        }

        TaskDefinition from = definition.from();

        RouteDefinition route = builder.from(from.uri());
        route.setBody().simple("Soya Camel Integration");

        Arrays.stream(definition.to()).forEach(e -> {
            route.to(e.uri());
        });

    }
}
