package soya.framework.restruts;

import java.util.concurrent.Callable;

public abstract class Action<T> implements Callable<T> {
    @RestActionParameter(name = "restActionContext",
            paramType = ParamType.WIRED_SERVICE)
    private RestActionContext restActionContext;

    protected RestActionContext getRestActionContext() {
        return restActionContext;
    }

}
