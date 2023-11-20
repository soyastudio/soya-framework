package soya.framework.commons.bean;

public interface DynaBean<T extends DynaClass> {
    T getDynaClass();

    void set(String name, Object value);

    Object get(String name);

    String getAsString(String name);

}
