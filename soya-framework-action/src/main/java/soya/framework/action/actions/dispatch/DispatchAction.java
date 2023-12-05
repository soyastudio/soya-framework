package soya.framework.action.actions.dispatch;

import soya.framework.action.*;
import soya.framework.action.actions.AnnotatedDynaAction;
import soya.framework.commons.util.DefaultUtils;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.commons.util.URIUtils;
import soya.framework.context.ServiceLocatorSingleton;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

public abstract class DispatchAction<T> extends AnnotatedDynaAction<T> {

    private Dispatcher<T> dispatcher;

    public DispatchAction() {
        super();
        DispatchActionDefinition dispatch = getClass().getAnnotation(DispatchActionDefinition.class);
        if (dispatch == null) {
            throw new IllegalArgumentException("Dispatch annotation is required.");
        }

        try {
            URI uri = new URI(dispatch.uri());
            String schema = uri.getScheme();
            String details = uri.getSchemeSpecificPart();

            ParameterMapping[] parameterMappings = new ParameterMapping[dispatch.propertyMappings().length];
            for (int i = 0; i < parameterMappings.length; i++) {
                PropertyMapping annotation = dispatch.propertyMappings()[i];
                parameterMappings[i] = new ParameterMapping(annotation.name(), annotation.type(), annotation.actionProperty());
            }

            if (schema.equals("action")) {
                dispatcher = new ActionDispatcher<T>(uri.getSchemeSpecificPart(), parameterMappings);

            } else if (schema.equals("bean")) {
                dispatcher = new BeanMethodDispatcher(details, parameterMappings);

            } else if (schema.equals("class")) {
                dispatcher = new StaticMethodDispatcher(details, parameterMappings);

            } else {
                throw new IllegalArgumentException("");
            }

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T call() throws Exception {
        return (T) dispatcher.dispatch(parameters, actionContext());
    }

    private ActionContext actionContext() {
        return ServiceLocatorSingleton.getInstance().getService(ActionContext.class);
    }

    static class ParameterMapping {
        private String name;
        private Class<?> type;
        private String mappedTo;

        ParameterMapping(String name, Class<?> type, String mappedTo) {
            this.name = name.isEmpty() ? mappedTo : name;
            this.type = type;
            this.mappedTo = mappedTo;
        }
    }

    static class ActionDispatcher<T> implements Dispatcher<T> {
        private final ActionName actionName;
        private final ParameterMapping[] parameterMappings;

        ActionDispatcher(String uri, ParameterMapping[] parameterMappings) {
            try {
                this.actionName = ActionName.fromURI(new URI(uri));
                this.parameterMappings = parameterMappings;

            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public T dispatch(Map<String, ActionParameter> parameters, ActionContext actionContext) throws Exception {
            ActionExecutor<T> executor = (ActionExecutor<T>) ActionClass.forName(actionName).executor(actionContext);
            Arrays.stream(parameterMappings).forEach(e -> {
                executor.set(e.name, parameters.get(e.mappedTo).get());

            });

            return executor.execute();
        }
    }

    static class BeanMethodDispatcher<T> implements Dispatcher<T> {

        private String bean;
        private String methodName;

        private Class<?>[] paramTypes;
        private String[] paramMappings;

        BeanMethodDispatcher(String details, ParameterMapping[] parameterMappings) {
            Class<?>[] paramTypes = new Class[parameterMappings.length];
            paramMappings = new String[parameterMappings.length];
            for (int i = 0; i < parameterMappings.length; i++) {
                ParameterMapping mapping = parameterMappings[i];
                if (DefaultUtils.isDefaultType(mapping.type)) {
                    throw new IllegalArgumentException("Parameter type need to be specified.");
                }
                paramTypes[i] = mapping.type;
                paramMappings[i] = mapping.mappedTo;
            }

            int mark = -1;
            if (details.contains("?")) {
                mark = details.indexOf('?');
                this.bean = details.substring(0, mark);
                String query = details.substring(mark + 1);
                this.methodName = URIUtils.splitQuery(query).get("method").get(0);
            }

            if (methodName == null) {
                throw new IllegalArgumentException("Method is not defined.");
            }

        }

        @Override
        public T dispatch(Map<String, ActionParameter> parameters, ActionContext actionContext) throws Exception {
            Object service = null;

            try {
                service = actionContext.getService(bean);

            } catch (ActionContextException e) {
                service = actionContext.getService(null, Class.forName(bean));

            }

            Method method = ReflectUtils.findMethod(service.getClass(), methodName, paramTypes);
            Object[] values = new Object[paramMappings.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = parameters.get(paramMappings[i]).get();
            }

            return (T) method.invoke(service, values);
        }
    }

    static class StaticMethodDispatcher<T> implements Dispatcher<T> {
        private Method method;
        private String[] paramMappings;

        StaticMethodDispatcher(String details, ParameterMapping[] parameterMappings) {
            Class<?>[] paramTypes = new Class[parameterMappings.length];
            paramMappings = new String[parameterMappings.length];
            for (int i = 0; i < parameterMappings.length; i++) {
                ParameterMapping mapping = parameterMappings[i];
                if (DefaultUtils.isDefaultType(mapping.type)) {
                    throw new IllegalArgumentException("Parameter type need to be specified.");
                }
                paramTypes[i] = mapping.type;
                paramMappings[i] = mapping.mappedTo;
            }

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

            try {
                method = ReflectUtils.findMethod(Class.forName(className), methodName, paramTypes);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public T dispatch(Map<String, ActionParameter> parameters, ActionContext actionContext) throws Exception {
            Object[] values = new Object[paramMappings.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = parameters.get(paramMappings[i]).get();
            }
            return (T) method.invoke(null, values);
        }
    }
}