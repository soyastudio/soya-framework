package soya.framework.restruts.reflect;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.MediaType;
import soya.framework.restruts.RestAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestAction(
        id = "runtime-stack-traces",
        path = "/restruts/runtime/stack-traces",
        method = HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        tags = "Restruct"
)
public class RuntimeStackTracesAction extends ReflectAction {
    @Override
    public Object call() throws Exception {
        JsonObject jsonObject = new JsonObject();
        Thread.getAllStackTraces().entrySet().forEach(e -> {
            JsonArray array = new JsonArray();
            for (StackTraceElement element : e.getValue()) {
                array.add(element.toString());
            }

            jsonObject.add(e.getKey().getName(), array);
        });
        return GSON.toJson(jsonObject);
    }

}
