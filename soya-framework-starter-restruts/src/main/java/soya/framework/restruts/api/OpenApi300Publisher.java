package soya.framework.restruts.api;

import com.google.gson.*;
import soya.framework.restruts.ActionMapping;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestActionContext;
import soya.framework.restruts.RestApiRenderer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class OpenApi300Publisher implements RestApiRenderer {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String render(RestActionContext registration) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("swagger.json");
        if (inputStream == null) {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/api/swagger_300.json");
        }
        JsonObject swagger = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
        JsonObject paths = new JsonObject();
        Arrays.stream(registration.getActionMappings()).forEach(a -> {
            String path = a.getPath().getPath();
            if (paths.get(path) == null) {
                paths.add(path, new JsonObject());
            }

            JsonObject pathObject = paths.get(path).getAsJsonObject();
            pathObject.add(a.getPath().getMethod(), createOperation(a));


        });

        swagger.add("paths", paths);
        return gson.toJson(swagger);
    }

    private JsonObject createOperation(ActionMapping mapping) {
        JsonObject operation = new JsonObject();
        if(mapping.getTags() != null) {
            JsonArray tags = new JsonArray();
            Arrays.stream(mapping.getTags()).forEach(t -> {
                tags.add(t);
            });
            operation.add("tags", tags);
        }
        operation.add("operationId", new JsonPrimitive(mapping.getId()));

        JsonArray params = new JsonArray();
        Arrays.stream(mapping.getParameters()).forEach(p -> {
            if (p.getParameterType().equals(ParamType.AUTO_WIRED)) {

            } else if (p.getParameterType().equals(ParamType.PAYLOAD)) {
                operation.add("requestBody", createRequestBody(p, mapping.getConsumes()));

            } else {
                params.add(createParameter(p));
            }
        });

        operation.add("parameters", params);

        // responses
        JsonObject responses = new JsonObject();
        JsonObject response = new JsonObject();
        response.addProperty("description", "Successful");
        JsonObject contents = new JsonObject();
        Arrays.stream(mapping.getProduces()).forEach(e -> {
            contents.add(e, new JsonObject());
        });
        response.add("content", contents);
        responses.add("200", response);
        operation.add("responses", responses);

        return operation;
    }

    private JsonObject createRequestBody(ActionMapping.ParameterMapping mapping, String[] contentTypes) {
        JsonObject body = new JsonObject();
        body.addProperty("description", mapping.getDescription());
        body.addProperty("required", true);


        JsonObject content = new JsonObject();
        Arrays.stream(contentTypes).forEach(c -> {
            content.add(c, new JsonObject());
        });
        body.add("content", content);
        return body;
    }

    private JsonObject createParameter(ActionMapping.ParameterMapping mapping) {
        JsonObject param = new JsonObject();
        param.addProperty("name", mapping.getReferredTo());
        if (mapping.getParameterType().equals(ParamType.HEADER_PARAM)) {
            param.addProperty("in", "header");
            param.add("schema", schema("string"));

        } else if (mapping.getParameterType().equals(ParamType.PATH_PARAM)) {
            param.addProperty("in", "path");
            param.add("schema", schema("string"));

        } else if (mapping.getParameterType().equals(ParamType.QUERY_PARAM)) {
            param.addProperty("in", "query");
            param.add("schema", schema("string"));

        } else if (mapping.getParameterType().equals(ParamType.PAYLOAD)) {
            param.addProperty("in", "body");
        }
        return param;
    }

    private JsonObject schema(String type) {
        JsonObject object = new JsonObject();
        object.addProperty("type", type);
        return object;
    }
}
