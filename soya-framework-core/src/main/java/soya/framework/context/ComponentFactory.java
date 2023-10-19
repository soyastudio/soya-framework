package soya.framework.context;

public interface ComponentFactory {
    <T> T create(Class<T> componentType, Context ctx);
}
