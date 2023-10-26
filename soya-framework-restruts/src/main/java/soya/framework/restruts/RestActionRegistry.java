package soya.framework.restruts;


import java.util.concurrent.Callable;

public final class RestActionRegistry {
    final String method;
    final String path;
    final Class<? extends Callable> actionClass;

    public RestActionRegistry(String method, String path, Class<? extends Callable> actionClass) {
        this.method = method;
        this.path = path;
        this.actionClass = actionClass;
    }

    public boolean match(final String method, final String path) {
        if (!method.equalsIgnoreCase(this.method)) {
            return false;

        } else if (path.equals(this.path)) {
            return true;

        }

        return false;
    }
}

