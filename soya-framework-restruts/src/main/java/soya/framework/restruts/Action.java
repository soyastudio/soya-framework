package soya.framework.restruts;

import java.util.concurrent.Callable;

public abstract class Action<T> implements Callable<T> {
    @RestActionParameter(name = "restActionContext",
            paramType = ParamType.AUTO_WIRED)
    private RestActionContext restActionContext;

    protected RestActionContext getRestActionContext() {
        return restActionContext;
    }

    protected Object getWireService(String name) {
        Object service = null;
        try {
            service = restActionContext.getDependencyInjector().getWiredResource(name);

        } catch (Exception e) {
            // do nothing
        }

        if(service == null) {
            try {
                Class<?> cls = Class.forName(name);
                service = restActionContext.getDependencyInjector().getWiredResource(null, Class.forName(name));
            } catch (Exception e) {
                // do nothing
            }
        }

        return service;
    }
}
