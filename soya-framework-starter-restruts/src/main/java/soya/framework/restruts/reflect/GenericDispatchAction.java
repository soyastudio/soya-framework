package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.RestActionParameter;

@RestAction(
        id = "dispatch",
        path = "/restruts/dispatch/service",
        method = HttpMethod.POST,
        parameters = {
                @RestActionParameter(name = "url", paramType = ParamType.HEADER_PARAM),
                @RestActionParameter(name = "input", paramType = ParamType.PAYLOAD)
        },
        produces = "text/plain",
        tags = "Restruct"
)
public class GenericDispatchAction extends ReflectAction {
    private String url;
    private String input;

    @Override
    public String call() throws Exception {
        MethodInvoker invoker = new MethodInvoker(url);
        Object result = invoker.execute(getRestActionContext(), input);
        if (result instanceof String) {
            return result.toString();
        } else {
            return GSON.toJson(result);
        }
    }
}
