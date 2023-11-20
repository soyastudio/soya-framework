package soya.framework.commons.bean;

public interface Annotatable {
    void annotate(String ns, Object annotation);

    Object getAnnotation(String ns);

    <T> T getAnnotation(Class<T> cls);

    String[] getAnnotations();
}
