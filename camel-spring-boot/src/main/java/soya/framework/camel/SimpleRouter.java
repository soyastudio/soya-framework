package soya.framework.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

public class SimpleRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        RouteDefinition definition = from("").to("");
    }
}
