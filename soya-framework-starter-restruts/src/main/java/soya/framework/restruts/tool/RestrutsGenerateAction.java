package soya.framework.restruts.tool;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionParameter;
import soya.framework.restruts.api.OpenApiRenderer;
import soya.framework.restruts.reflect.ReflectAction;

@RestAction(
        id = "actions-generator",
        path = "/restruts/tool/actions-generator",
        method = HttpMethod.POST,
        parameters = {
                @RestActionParameter(name = "input", paramType = ParamType.PAYLOAD)
        },
        produces = "text/plain",
        tags = "Restruct"
)
public class RestrutsGenerateAction extends ReflectAction {
    private String input;

    @Override
    public Object call() throws Exception {
        SwaggerParseResult result = new OpenAPIParser().readContents(input, null, null);
        OpenApiRenderer renderer = new OpenApiRenderer();

        OpenAPI openAPI = result.getOpenAPI();

        System.out.println(renderer.toJson(openAPI));

        return "TODO: generate annotations of @RestAction which can be applied to @RestActionDomain or Action classes.";
    }
}
