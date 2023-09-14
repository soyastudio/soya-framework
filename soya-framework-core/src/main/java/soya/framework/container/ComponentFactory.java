package soya.framework.container;

public interface ComponentFactory {
    <T> T create(Class<T> componentType, Context ctx);
}
