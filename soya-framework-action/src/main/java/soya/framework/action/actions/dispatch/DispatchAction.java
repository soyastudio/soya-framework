package soya.framework.action.actions.dispatch;

import soya.framework.action.actions.AnnotatedDynaAction;
import soya.framework.commons.conversion.ConvertUtils;
import soya.framework.commons.util.URIUtils;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class DispatchAction<T> extends AnnotatedDynaAction<T> {

    private final MethodInvoker invoker;

    public DispatchAction() {
        super();
        DispatchActionDefinition dispatch = getClass().getAnnotation(DispatchActionDefinition.class);
        if (dispatch == null) {
            throw new IllegalArgumentException("Dispatch annotation is required.");
        }

        try {
            URI uri = new URI(dispatch.uri());
            String schema = uri.getScheme();

            if(schema.equals(ActionMethodInvoker.schema)) {

            } else if(schema.equals(BeanMethodInvoker.schema)) {

            } else if(schema.equals(StaticMethodInvoker.schema)) {

            } else {
                throw new IllegalArgumentException("URI is not supported: " + dispatch.uri());
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
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
            values[i] = ConvertUtils.convert(getParameter(invoker.paramMappings[i]), invoker.paramImplTypes[i]);
        }

        return (T) method.invoke(instance, values);
    }

    static class ActionMethodInvoker {
        private static String schema = "action";

    }

    static class BeanMethodInvoker {
        private static String schema = "bean";

    }

    static class StaticMethodInvoker {
        private static String schema = "class";

    }

    static class  Invoker {

        private String type;
        private String methodName;
        private Class<?>[] parameterTypes;
        private String[] parameterMappings;

        protected Method method;

         protected Invoker(String type, String methodName, Class<?>[] parameterTypes, String[] parameterMappings) {
            this.type = type;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.parameterMappings = parameterMappings;
        }

    }

    static class MethodInvoker {

        private Method method;
        private Object instance;

        private Class<?>[] paramTypes;
        private Class<?>[] paramImplTypes;
        private String[] paramMappings;

        MethodInvoker(DispatchActionDefinition dispatch) throws Exception {
            DispatchActionParameter[] parameters = dispatch.parameters();
            int len = parameters.length;
            paramTypes = new Class[len];
            paramImplTypes = new Class[len];
            paramMappings = new String[len];
            for (int i = 0; i < len; i++) {
                DispatchActionParameter parameter = parameters[i];
                paramTypes[i] = parameter.type();
                paramImplTypes[i] = parameter.implType();
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

            if (schema.equals("action")) {


            } else if (schema.equals("bean")) {


            } else if (schema.equals("class")) {
                Class<?> cls = Class.forName(className);
                this.method = cls.getDeclaredMethod(methodName, paramTypes);

            } else {
                throw new IllegalArgumentException("Schema is not supported for dispatch action: " + schema);
            }

        }
    }
}