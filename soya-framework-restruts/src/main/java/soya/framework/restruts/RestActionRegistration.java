package soya.framework.restruts;

public interface RestActionRegistration<T> {
    void register(T resource);

    class Registry {
        String method;
        String path;
        String action;

    }
}
