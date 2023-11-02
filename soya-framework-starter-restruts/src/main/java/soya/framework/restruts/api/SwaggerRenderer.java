package soya.framework.restruts.api;

import com.google.gson.*;
import soya.framework.restruts.RestActionContext;
import soya.framework.restruts.RestApiRenderer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SwaggerRenderer implements RestApiRenderer {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String render(RestActionContext registration) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("swagger.json");
        if(inputStream == null) {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/api/swagger.json");
        }
        JsonObject swagger = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();


        JsonObject paths = new JsonObject();
        Arrays.stream(registration.getActionMappings()).forEach(a -> {
            String path = a.getPath().getPath();
            if(paths.get(path) == null) {
                paths.add(path, new JsonObject());
            }

            JsonObject pathObject = paths.get(path).getAsJsonObject();
            JsonObject operation = new JsonObject();
            operation.add("operationId", new JsonPrimitive(a.getId()));

            JsonObject responses = new JsonObject();

            responses.add("200", new JsonObject());
            operation.add("responses", responses);
            pathObject.add(a.getPath().getMethod(), operation);



        });

        swagger.add("paths", paths);

        return new GsonBuilder().setPrettyPrinting().create().toJson(swagger);
    }
}
