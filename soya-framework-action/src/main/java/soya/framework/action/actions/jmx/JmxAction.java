package soya.framework.action.actions.jmx;

import java.util.concurrent.Callable;

public abstract class JmxAction<T> implements Callable<T> {
    @Override
    public T call() throws Exception {
        return execute();
    }

    protected abstract T execute() throws Exception;
}
