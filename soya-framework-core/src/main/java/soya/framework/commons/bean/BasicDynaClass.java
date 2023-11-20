package soya.framework.commons.bean;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicDynaClass extends DynaClassBase {

    public BasicDynaClass() {
        setProperties(new DynaProperty[0]);
    }

    public BasicDynaClass(String name, DynaProperty[] properties) {
        super();
        setName(name);
        setProperties(properties);
    }

    @Override
    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return new BasicDyanBean(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static void main(String[] args) throws Exception {
        DynaClass dynaClass = BasicDynaClass.builder().copyFromBeanProperties(DynaClass.class).create();
        for (DynaProperty property : dynaClass.getDynaProperties()) {
            System.out.println("============= " + property.getName() + ": " + property.getType().getName());
        }

    }

    static class BasicDyanBean extends DynaBeanBase<BasicDynaClass> {
        protected BasicDyanBean(BasicDynaClass dynaClass) {
            super(dynaClass);
        }
    }

    public static class Builder {
        private String name;
        private Set<DynaProperty> properties = new LinkedHashSet<>();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addProperty(DynaProperty dynaProperty) {
            properties.add(dynaProperty);
            return this;
        }

        public Builder copyFromDynaClass(DynaClass dynaClass) {
            for(DynaProperty property : dynaClass.getDynaProperties()) {
                properties.add(property);
            }
            return this;
        }

        public Builder copyFromBeanProperties(Class<?> protoType) throws IntrospectionException {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(protoType).getPropertyDescriptors();
            for (PropertyDescriptor propDesc : propertyDescriptors) {
                if (!propDesc.getName().equals("class")) {
                    properties.add(new DynaProperty(propDesc.getName(), propDesc.getPropertyType()));
                }
            }

            if (protoType.isInterface()) {
                Class<?>[] parents = protoType.getInterfaces();
                if (parents != null && parents.length > 0) {
                    for (Class<?> p : parents) {
                        copyFromBeanProperties(p);
                    }
                }

            }
            return this;
        }

        public Builder copyFromDeclaredFields(Class<?> cls) {
            for (Field field : cls.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {

                    properties.add(new DynaProperty(field.getName(), field.getType()));
                }
            }

            return this;
        }

        public Builder copyFromFields(Class<?> cls) {
            for (Class<?> parent = cls; !parent.equals(Object.class); parent = parent.getSuperclass()) {
                copyFromDeclaredFields(parent);
            }

            return this;
        }

        public BasicDynaClass create() {
            return new BasicDynaClass(name, properties.toArray(new DynaProperty[properties.size()]));
        }
    }
}
