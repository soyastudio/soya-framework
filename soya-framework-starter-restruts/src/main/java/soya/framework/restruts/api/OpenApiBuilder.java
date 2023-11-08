package soya.framework.restruts.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@OpenAPIDefinition(
        info = @Info(),
        tags = {},
        servers = {},
        security = {},
        externalDocs = @ExternalDocumentation,
        extensions = {}

)
public class OpenApiBuilder {

    public OpenAPI build() {

        return new OpenAPI();
    }
}
