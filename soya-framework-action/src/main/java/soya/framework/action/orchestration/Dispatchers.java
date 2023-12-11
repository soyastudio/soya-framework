package soya.framework.action.orchestration;

import soya.framework.action.*;
import soya.framework.action.orchestration.annotation.TaskDefinition;
import soya.framework.commons.util.DefaultUtils;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.commons.util.URIUtils;
import soya.framework.context.ServiceLocatorSingleton;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Dispatchers {

    public static Dispatcher create(TaskDefinition definition) {
        try {
            URI uri = new URI(definition.uri());
            String schema = uri.getScheme();
            String details = uri.getSchemeSpecificPart();

            ParameterMapping[] parameterMappings = new ParameterMapping[definition.parameterMappings().length];
            for (int i = 0; i < parameterMappings.length; i++) {
                soya.framework.action.orchestration.annotation.ParameterMapping annotation = definition.parameterMappings()[i];
                parameterMappings[i] = new ParameterMapping(annotation.name(), annotation.type(), annotation.mappingTo());
            }

            if (schema.equals("action")) {
                return new ActionDispatcher(uri.getSchemeSpecificPart(), parameterMappings);

            } else if (schema.equals("bean")) {
                return new BeanMethodDispatcher(details, parameterMappings);

            } else if (schema.equals("class")) {
                return new StaticMethodDispatcher(details, parameterMappings);

            } else {
                throw new IllegalArgumentException("");
            }


        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static class ParameterMapping {
        private String name;
        private Class<?> type;
        private String mappingTo;

        ParameterMapping(String name, Class<?> type, String mappingTo) {
            this.name = name.isEmpty() ? mappingTo : name;
            this.type = type;
            this.mappingTo = mappingTo;
        }
    }

    static class ActionDispatcher implements Dispatcher {
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
        public Object dispatch(Session session) throws Exception {
            ActionContext actionContext = ServiceLocatorSingleton.getInstance().getService(ActionContext.class);
            ActionExecutor executor = (ActionExecutor) ActionClass.forName(actionName).executor(actionContext);
            Arrays.stream(parameterMappings).forEach(e -> {
                String mappingTo = e.mappingTo;
                Object value = null;
                if(mappingTo.endsWith("}")) {
                    value = session.getParameter(mappingTo);
                } else {
                    value = session.evaluate(e.mappingTo);

                }
                executor.set(e.name, value);
            });

            return executor.execute();
        }
    }

    static class BeanMethodDispatcher implements Dispatcher {

        private String bean;
        private String methodName;

        private Class<?>[] paramTypes;
        private String[] paramMappings;

        BeanMethodDispatcher(String details, ParameterMapping[] parameterMappings) {
            paramTypes = new Class[parameterMappings.length];
            paramMappings = new String[parameterMappings.length];
            for (int i = 0; i < parameterMappings.length; i++) {
                ParameterMapping mapping = parameterMappings[i];
                if (DefaultUtils.isDefaultType(mapping.type)) {
                    throw new IllegalArgumentException("Parameter type need to be specified.");
                }
                paramTypes[i] = mapping.type;
                paramMappings[i] = mapping.mappingTo;
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
        public Object dispatch(Session session) throws Exception {
            Object service = null;
            ActionContext actionContext = ServiceLocatorSingleton.getInstance().getService(ActionContext.class);
            try {
                service = actionContext.getService(bean);

            } catch (ActionContextException e) {
                service = actionContext.getService(null, Class.forName(bean));

            }

            Method method = ReflectUtils.findMethod(service.getClass(), methodName, paramTypes);

            Object[] values = new Object[paramMappings.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = session.getParameter(paramMappings[i]);
            }

            return method.invoke(service, values);
        }
    }

    static class StaticMethodDispatcher implements Dispatcher {
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
                paramMappings[i] = mapping.mappingTo;
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
        public Object dispatch(Session session) throws Exception {
            Object[] values = new Object[paramMappings.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = session.getParameter(paramMappings[i]);
            }
            return method.invoke(null, values);
        }
    }
}
