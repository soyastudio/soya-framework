package soya.framework.restruts.api;

import com.google.gson.*;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.io.IOUtils;
import soya.framework.restruts.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class OpenApiRenderer implements RestApiRenderer {
    protected static Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    @Override
    public String render(RestActionContext registration) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("openapi.yaml");
        if (inputStream == null) {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/api/openapi.yaml");
        }

        SwaggerParseResult result = new OpenAPIParser().readContents(IOUtils.toString(inputStream, Charset.defaultCharset()), null, null);
        OpenApiRenderer renderer = new OpenApiRenderer();

        OpenAPI openAPI = result.getOpenAPI();

        // Paths:
        openAPI.setPaths(new Paths());
        Paths paths = openAPI.getPaths();
        Arrays.stream(registration.getActionMappings()).forEach(e -> {
            ActionMapping.Path path = e.getPath();
            PathItem pathItem = paths.get(path.getPath());
            if (pathItem == null) {
                pathItem = new PathItem();
                paths.put(path.getPath(), pathItem);
            }

            Operation operation = createOperation(e);
            if (path.getMethod().equalsIgnoreCase("get")) {
                pathItem.get(operation);

            } else if (path.getMethod().equalsIgnoreCase("post")) {
                pathItem.post(operation);

            } else if (path.getMethod().equalsIgnoreCase("put")) {
                pathItem.put(operation);

            } else if (path.getMethod().equalsIgnoreCase("delete")) {
                pathItem.delete(operation);

            }
        });

        // Components:
        openAPI.setComponents(new Components());

        return toJson(openAPI);
    }

    private Operation createOperation(ActionMapping mapping) {

        Operation operation = new Operation();
        operation.operationId(mapping.getId());
        operation.setTags(Arrays.asList(mapping.getTags()));

        Arrays.stream(mapping.getParameters()).forEach(e -> {
            if(e.getParameterType().equals(ParamType.PAYLOAD)) {
                RequestBody requestBody = new RequestBody()
                        .content(new Content()
                                .addMediaType(mapping.getConsumes()[0], new MediaType()));
                operation.requestBody(requestBody);

            } else {
                Parameter parameter = createParameter(e);
                if(parameter != null) {
                    operation.addParametersItem(parameter);
                }
            }

        });

        ApiResponses apiResponses = new ApiResponses();
        ApiResponse response = new ApiResponse();
        response.content(new Content().addMediaType(mapping.getProduces()[0], new MediaType()));
        apiResponses.addApiResponse("200", response);
        operation.responses(apiResponses);

        return operation;
    }

    private Parameter createParameter(ActionMapping.ParameterMapping pm) {
        Parameter parameter = null;
        if (pm.getParameterType().equals(ParamType.QUERY_PARAM)) {
            parameter = new QueryParameter();


        } else if (pm.getParameterType().equals(ParamType.PATH_PARAM)) {
            parameter = new PathParameter();

        } else if (pm.getParameterType().equals(ParamType.HEADER_PARAM)) {
            parameter = new HeaderParameter();

        }

        if(parameter != null) {
            parameter.name(pm.getReferredTo());
            parameter.schema(new Schema().type("string"));
            parameter.description(pm.getDescription());
        }

        return parameter;
    }

    public String toJson(OpenAPI openAPI) {
        JsonObject root = new JsonObject();

        // openapi
        root.addProperty("openapi", openAPI.getOpenapi());

        // info:
        root.add("info", GSON.toJsonTree(openAPI.getInfo()));

        // servers:
        root.add("servers", GSON.toJsonTree(openAPI.getServers()));

        //Tags:
        root.add("tags", GSON.toJsonTree(openAPI.getTags()));

        // Paths
        root.add("paths", paths(openAPI.getPaths()));

        // Components:
        root.add("components", GSON.toJsonTree(openAPI.getComponents()));

        return GSON.toJson(root);
    }

    private JsonElement paths(Paths paths) {
        JsonObject object = new JsonObject();
        paths.entrySet().forEach(e -> {
            object.add(e.getKey(), pathItem(e.getValue()));
        });

        return object;
    }

    private JsonElement pathItem(PathItem pathItem) {
        JsonObject object = new JsonObject();
        if (pathItem.getGet() != null) {
            object.add("get", operation(pathItem.getGet()));
        }

        if (pathItem.getPost() != null) {
            object.add("post", operation(pathItem.getPost()));
        }

        if (pathItem.getPut() != null) {
            object.add("put", operation(pathItem.getPut()));
        }

        if (pathItem.getDelete() != null) {
            object.add("delete", operation(pathItem.getDelete()));
        }

        return object;
    }

    private JsonElement operation(Operation operation) {
        JsonObject object = new JsonObject();
        object.addProperty("summary", operation.getSummary());
        object.addProperty("operationId", operation.getOperationId());
        object.add("tags", GSON.toJsonTree(operation.getTags()));

        if (operation.getParameters() != null && operation.getParameters().size() > 0) {
            JsonArray array = new JsonArray();
            operation.getParameters().forEach(e -> {
                array.add(parameter(e));
            });
            object.add("parameters", array);
        }

        if (operation.getRequestBody() != null) {
            object.add("requestBody", GSON.toJsonTree(operation.getRequestBody()));
        }

        object.add("responses", GSON.toJsonTree(operation.getResponses()));

        return object;
    }

    private JsonElement parameter(Parameter parameter) {
        JsonObject object = new JsonObject();
        object.addProperty("name", parameter.getName());
        if (parameter instanceof QueryParameter) {
            object.addProperty("in", "query");

        } else if (parameter instanceof HeaderParameter) {
            object.addProperty("in", "header");

        } else if (parameter instanceof PathParameter) {
            object.addProperty("in", "path");

        } else if (parameter instanceof CookieParameter) {
            object.addProperty("in", "cookie");

        }

        object.addProperty("description", parameter.getDescription());
        object.addProperty("required", parameter.getRequired());
        if (parameter.getSchema() != null) {
            object.add("schema", GSON.toJsonTree(parameter.getSchema()));
        }

        return object;
    }
}
