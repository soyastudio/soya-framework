package soya.framework.action.flow;

import soya.framework.action.ActionName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public abstract class PipelineAction<T> implements Callable<T> {
    private ActionName actionName;

    protected PipelineAction() {
    }

    @Override
    public T call() throws Exception {
        return null;
    }

    protected abstract T process(Session session);
}
