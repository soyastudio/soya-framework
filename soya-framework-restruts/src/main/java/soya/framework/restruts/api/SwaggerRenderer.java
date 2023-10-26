package soya.framework.restruts.api;

import com.google.gson.GsonBuilder;
import soya.framework.restruts.RestActionRegistration;
import soya.framework.restruts.RestApiRenderer;

public class SwaggerRenderer implements RestApiRenderer {
    @Override
    public String render(RestActionRegistration registration) {
        Swagger.SwaggerBuilder builder = Swagger.builder();

        Swagger swagger = builder.build();

        return new GsonBuilder().setPrettyPrinting().create().toJson(swagger);
    }
}
