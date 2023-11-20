package soya.framework.commons.bean;

public interface DynaBeanExecutor<T> {
    T execute(DynaBean<?> bean);
}
