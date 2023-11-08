package soya.framework.restruts.reflect;

import soya.framework.restruts.HttpMethod;
import soya.framework.restruts.MediaType;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.springboot.DependentInjector;

@RestAction(
        id = "services",
        path = "/restruts/services",
        method = HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        tags = "Restruct"
)
public class ServiceRegistrationsAction extends ReflectAction {
    @Override
    public String call() throws Exception {
        DependentInjector injector = (DependentInjector) getRestActionContext();
        return GSON.toJson(injector.getServiceRegistrations());
    }
}
