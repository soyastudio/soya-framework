package soya.framework.commons.bean;

import java.util.HashMap;
import java.util.Map;

class AnnotatableFeature implements Annotatable {
    private transient final Map<String, Object> annotations = new HashMap<String, Object>();

    public void annotate(String ns, Object annotation) {
        annotations.put(ns, annotation);
    }

    public Object getAnnotation(String ns) {
        return annotations.get(ns);
    }

    public <T> T getAnnotation(Class<T> cls) {
        return (T) annotations.get(cls.getName());
    }

    @Override
    public String[] getAnnotations() {
        return annotations.keySet().toArray(new String[annotations.size()]);
    }
}
