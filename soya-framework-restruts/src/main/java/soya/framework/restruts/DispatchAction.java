package soya.framework.restruts;

import soya.framework.restruts.util.ConvertUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DispatchAction<T> extends Action<T> {

    protected DispatchAction() {
    }

    protected DispatchAction(ActionMapping mapping) {
    }

    protected Map<String, DynaProperty> properties = new LinkedHashMap<>();

    public String[] getPropertyNames() {
        return properties.keySet().toArray(new String[properties.size()]);
    }

    public Class<?> getPropertyType(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Property not defined: " + name);
        }
        return properties.get(name).getType();
    }

    public void setProperty(String name, Object value) {
        if (properties.containsKey(name)) {
            properties.get(name).setValue(value);
        }
    }

    public Object getPropertyValue(String name) {
        if (properties.containsKey(name)) {
            return properties.get(name).getValue();
        } else {
            return null;
        }
    }


    protected static class DynaProperty {
        private final String name;
        private final Class<?> type;
        private Object value;

        public DynaProperty(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = ConvertUtils.convert(value, type);
        }
    }

}
