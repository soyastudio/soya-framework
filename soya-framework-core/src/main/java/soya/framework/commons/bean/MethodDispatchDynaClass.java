package soya.framework.commons.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.Callable;

public class MethodDispatchDynaClass extends DynaClassBase {

    private Method method;
    private Object instance;

    public MethodDispatchDynaClass(String name, Method method, Object instance) {
        super();
        this.method = method;

        Parameter[] parameters = method.getParameters();
        DynaProperty[] properties = new DynaProperty[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            properties[i] = new DynaProperty(parameters[i].getName(), parameters[i].getType());
        }

        setName(name);
        setProperties(properties);
    }


    @Override
    public MethodDispatchDynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return new MethodDispatchDynaBean(this);
    }

    public static class MethodDispatchDynaBean extends DynaBeanBase<MethodDispatchDynaClass> implements Callable<Object> {

        protected MethodDispatchDynaBean(MethodDispatchDynaClass dynaClass) {
            super(dynaClass);
        }

        public Object invoke(Object instance) throws InvocationTargetException, IllegalAccessException {
            return getDynaClass().method.invoke(instance, parameterValues());
        }

        @Override
        public Object call() throws Exception {
            return getDynaClass().method.invoke(getDynaClass().instance, parameterValues());
        }

        protected Object[] parameterValues() {
            DynaProperty[] properties = getDynaClass().getDynaProperties();
            Object[] paramValues = new Object[properties.length];
            for (int i = 0; i < properties.length; i++) {
                paramValues[i] = get(properties[i].getName());
            }
            return paramValues;
        }
    }

}
