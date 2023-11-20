package soya.framework.commons.bean;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class CommandDispatchDynaClass extends DynaClassBase {

    private Class<?> commandType;
    private Method method;

    protected CommandDispatchDynaClass() {
        super();
    }

    public CommandDispatchDynaClass(String name, Class<?> commandType, String[] propNames, String methodName) {
        super();
        this.commandType = commandType;
        try {
            this.method = commandType.getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DynaProperty[] dynaProperties = new DynaProperty[propNames.length];

        setName(name);
        setProperties(dynaProperties);

    }

    @Override
    public CommandDispatchDynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return new CommandDispatchDynaBean(this);
    }

    public static class CommandDispatchDynaBean extends DynaBeanBase<CommandDispatchDynaClass> implements Callable<Object> {

        protected CommandDispatchDynaBean(CommandDispatchDynaClass dynaClass) {
            super(dynaClass);
        }

        @Override
        public Object call() throws Exception {
            Object instance = getDynaClass().commandType.newInstance();
            // TODO: set instance properties
            return getDynaClass().method.invoke(instance, new Object[0]);
        }
    }

}
