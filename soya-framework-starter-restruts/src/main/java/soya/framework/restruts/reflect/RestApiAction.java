package soya.framework.restruts.reflect;

import soya.framework.restruts.Action;
import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.RestAction;

@RestAction(
        id = "api",
        path = "/restruts/api",
        method = HttpMethod.GET,
        produces = "application/json",
        tags = "Restruct"
)
public class RestApiAction extends Action<String> {
    @Override
    public String call() throws Exception {
        return getRestActionContext().getApi();
    }
}
