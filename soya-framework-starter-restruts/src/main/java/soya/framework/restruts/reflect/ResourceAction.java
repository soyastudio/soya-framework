package soya.framework.restruts.reflect;

import soya.framework.restruts.*;

@RestAction(
        id = "resource",
        path = "/restruts/resource",
        method = HttpMethod.POST,
        parameters = {
                @RestActionParameter(name = "url", paramType = ParamType.PAYLOAD)
        },
        produces = "text/plain",
        tags = "Restruct"
)
public class ResourceAction extends Action<String> {

    private String url;

    @Override
    public String call() throws Exception {
        return getRestActionContext().getDependencyInjector().getWiredResource(url, String.class);
    }
}
