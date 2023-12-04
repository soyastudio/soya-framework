package soya.framework.action.actions.dispatch;

import soya.framework.action.actions.AnnotatedDynaAction;
import soya.framework.commons.conversion.ConvertUtils;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.commons.util.URIUtils;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;
import soya.framework.context.ServiceLocatorSingleton;

import java.lang.reflect.Method;
import java.net.URI;

public abstract class DispatchAction<T> extends AnnotatedDynaAction<T> {

    private final MethodInvoker invoker;

    public DispatchAction() {
        super();
        DispatchActionDefinition dispatch = getClass().getAnnotation(DispatchActionDefinition.class);
        if (dispatch == null) {
            throw new IllegalArgumentException("Dispatch annotation is required.");
        }

        try {
            invoker = new MethodInvoker(dispatch);

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T call() throws Exception {
        Method method = invoker.method;
        Object instance = invoker.instance;
        Object[] values = new Object[invoker.paramTypes.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = getParameter(invoker.paramMappings[i]);
        }

        return (T) method.invoke(instance, values);
    }

    static class MethodInvoker {

        private Method method;
        private Object instance;

        private Class<?>[] paramTypes;
        private String[] paramMappings;

        MethodInvoker(DispatchActionDefinition dispatch) throws Exception {
            DispatchActionParameter[] parameters = dispatch.parameters();
            int len = parameters.length;
            paramTypes = new Class[len];
            paramMappings = new String[len];
            for (int i = 0; i < len; i++) {
                DispatchActionParameter parameter = parameters[i];
                paramTypes[i] = parameter.type();
                paramMappings[i] = parameter.actionParameter();
            }

            URI uri = new URI(dispatch.uri());

            String schema = uri.getScheme();
            String details = uri.getSchemeSpecificPart();

            String className = null;
            String methodName = null;

            int mark = -1;
            if (details.contains("?")) {
                mark = details.indexOf('?');
                className = details.substring(0, mark);
                String query = details.substring(mark + 1);
                try {
                    methodName = URIUtils.splitQuery(query).get("method").get(0);
                } catch (Exception e) {

                }
            }

            if (schema.equals("bean")) {
                this.instance = findService(className);
                this.method = ReflectUtils.findMethod(instance.getClass(), methodName, paramTypes);

            } else if (schema.equals("class")) {
                Class<?> cls = Class.forName(className);
                this.method = cls.getDeclaredMethod(methodName, paramTypes);

            } else {
                throw new IllegalArgumentException("Schema is not supported for dispatch action: " + schema);
            }

        }

        private Object findService(String bean) {
            ServiceLocator locator = ServiceLocatorSingleton.getInstance();
            try {
                return locator.getService(bean);

            } catch (ServiceLocateException e) {
                try {
                    return locator.getService(Class.forName(bean));

                } catch (ServiceLocateException | ClassNotFoundException ex) {
                    throw new RuntimeException("Cannot find service bean: " + bean);

                }
            }
        }
    }
}