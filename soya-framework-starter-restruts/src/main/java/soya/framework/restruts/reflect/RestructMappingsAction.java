package soya.framework.restruts.reflect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.restruts.Action;
import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionContext;

@RestAction(
        id = "actions",
        path = "/restruts/actions",
        method = HttpMethod.GET,
        produces = "application/json",
        tags = "Restruct"
)
public class RestructMappingsAction extends Action<String> {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String call() throws Exception {
        RestActionContext context = getRestActionContext();
        return GSON.toJson(context.getActionMappings());
    }
}
