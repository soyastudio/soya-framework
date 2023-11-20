package soya.framework.commons.bean;


import org.apache.commons.beanutils.ConvertUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynaClassBase extends AnnotatableFeature implements DynaClass {

    private String name;
    private Map<String, DynaProperty> propertyMap = new LinkedHashMap<>();

    protected DynaClassBase() {
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setProperties(DynaProperty[] properties) {
        if (properties != null) {
            for (DynaProperty property : properties) {
                propertyMap.put(property.getName(), property);
            }
        }
    }

    public DynaProperty[] getDynaProperties() {
        return propertyMap.values().toArray(new DynaProperty[propertyMap.size()]);
    }

    public DynaProperty getDynaProperty(String name) {
        return propertyMap.get(name);
    }

    protected boolean contains(String name) {
        return propertyMap.containsKey(name);
    }

    protected static class DynaBeanBase<T extends DynaClassBase> implements DynaBean<T> {
        private final transient T dynaClass;
        private final HashMap<String, Object> values = new HashMap<>();

        protected DynaBeanBase(T dynaClass) {
            this.dynaClass = dynaClass;
        }

        @Override
        public T getDynaClass() {
            return dynaClass;
        }

        @Override
        public void set(String name, Object value) {
            if (!dynaClass.contains(name)) {
                throw new IllegalArgumentException("No such property: " + name);
            }
            values.put(name, ConvertUtils.convert(value, dynaClass.getDynaProperty(name).getType()));
        }

        @Override
        public Object get(String name) {
            if (!dynaClass.contains(name)) {
                throw new IllegalArgumentException("No such property: " + name);
            }
            return values.get(name);
        }

        @Override
        public String getAsString(String name) {
            if (!dynaClass.contains(name)) {
                throw new IllegalArgumentException("No such property: " + name);
            }

            if(!values.containsKey(name)) {
                return null;
            }

            Object value = values.get(name);
            DynaProperty property = dynaClass.getDynaProperty(name);
            return String.valueOf(value);
        }
    }
}
