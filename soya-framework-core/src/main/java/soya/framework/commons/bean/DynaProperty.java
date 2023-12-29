package soya.framework.commons.bean;

import java.io.Serializable;

public final class DynaProperty extends AnnotatableFeature implements Serializable {

    private final String name;
    private final transient Class<?> type;

    public DynaProperty(final String name, final Class<?> type) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}
