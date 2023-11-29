package soya.framework.action;

import soya.framework.commons.conversion.ConvertUtils;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public final class ActionClass {

    private static Logger logger = Logger.getLogger(ActionClass.class.getName());

    private static Map<ActionName, ActionClass> registrations = new LinkedHashMap<>();
    private static Map<ActionName, ActionFactory> creators = new HashMap<>();

    private static Map<Class<?>, ActionFactory> factories = new HashMap<>();
    private static DefaultActionFactory defaultActionFactory = new DefaultActionFactory();

    private final Class<? extends Callable> actionType;
    private final ActionName actionName;
    private final Map<String, ActionProperty> params;

    private final ActionProperty[] unwired;

    ActionClass(Class<? extends Callable> actionType, ActionName actionName, List<ActionProperty> parameters) {
        this.actionName = actionName;
        this.actionType = actionType;

        Map<String, ActionProperty> map = new LinkedHashMap<>();
        List<ActionProperty> unwiredList = new ArrayList<>();
        parameters.forEach(e -> {
            map.put(e.getName(), e);
            if (!e.getParameterType().isWired()) {
                unwiredList.add(e);
            }
        });

        this.params = Collections.unmodifiableMap(map);
        this.unwired = unwiredList.toArray(new ActionProperty[unwiredList.size()]);
    }

    public Class<? extends Callable> getActionType() {
        return actionType;
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String[] parameterNames() {
        return params.keySet().toArray(new String[params.size()]);
    }

    public ActionParameterType parameterType(String name) {
        if (!params.containsKey(name)) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not defined for action class: " + actionName);
        }
        return params.get(name).getParameterType();
    }

    public String referredTo(String name) {
        if (!params.containsKey(name)) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not defined for action class: " + actionName);
        }
        return params.get(name).getReferredTo();
    }

    public boolean required(String name) {
        if (!params.containsKey(name)) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not defined for action class: " + actionName);
        }
        return params.get(name).isRequired();
    }

    public String description(String name) {
        if (!params.containsKey(name)) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not defined for action class: " + actionName);
        }
        return params.get(name).getDescription();
    }

    ActionProperty[] parameters() {
        return params.values().toArray(new ActionProperty[params.size()]);
    }

    ActionProperty getParameter(String name) {
        return params.get(name);
    }

    static void register(ActionFactory factory) {
        Class<?> type = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        factories.put(type, factory);
    }

    static void register(ActionClass actionClass) {
        ActionName actionName = actionClass.getActionName();
        if (!registrations.containsKey(actionName)) {
            registrations.put(actionName, actionClass);
        }
        creators.put(actionName, findActionFactory(actionClass.getActionType()));
    }

    private static ActionFactory findActionFactory(Class<?> cls) {
        for (Map.Entry<Class<?>, ActionFactory> entry : factories.entrySet()) {
            if (entry.getKey().isAssignableFrom(cls)) {
                return entry.getValue();
            }
        }

        return defaultActionFactory;
    }

    public ActionExecutor<?> newInstance(ActionContext actionContext) {
        return new ActionExecutor(actionName, creators.get(actionName).create(actionName, actionContext), unwired);
    }

    public static ActionName[] actionNames() {
        List<ActionName> actionNames = new ArrayList<>(registrations.keySet());
        Collections.sort(actionNames);
        return actionNames.toArray(new ActionName[registrations.size()]);
    }

    public static ActionClass forName(ActionName actionName) {
        if (!registrations.containsKey(actionName)) {
            throw new IllegalArgumentException("Action does not exist: " + actionName);
        }

        return registrations.get(actionName);
    }

    static class DefaultActionFactory implements ActionFactory {

        @Override
        public Callable<?> create(ActionName actionName, ActionContext actionContext) {
            try {
                ActionClass actionClass = ActionClass.forName(actionName);
                Class<?> actionType = actionClass.getActionType();
                Callable<?> callable = (Callable<?>) actionType.newInstance();
                if (DynaAction.class.isAssignableFrom(actionType)) {
                    Arrays.stream(actionClass.parameters()).forEach(e -> {
                        DynaAction dynaAction = (DynaAction) callable;
                        Object value = null;
                        if (!e.getParameterType().isWired()) {
                            // set from input:

                        } else if (ActionParameterType.WIRED_VALUE.equals(e.getParameterType())) {
                            value = e.getReferredTo();

                        } else if (ActionParameterType.WIRED_PROPERTY.equals(e.getParameterType())) {
                            value = actionContext.getProperty(e.getReferredTo(), e.isRequired());

                        } else if (ActionParameterType.WIRED_SERVICE.equals(e.getParameterType())) {
                            try {
                                // FIXME:
                                value = actionContext.getService(e.getReferredTo(), e.getType());

                            } catch (NotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else if (ActionParameterType.WIRED_RESOURCE.equals(e.getParameterType())) {
                            // FIXME:
                            value = actionContext.getResource(e.getReferredTo(), e.getType());

                        }

                        if (value != null) {
                            dynaAction.setParameter(e.getName(), value);
                        }
                    });

                } else {
                    Arrays.stream(actionClass.parameters()).forEach(e -> {
                        Field field = ReflectUtils.findField(actionType, e.getName());
                        Object value = null;
                        if (!e.getParameterType().isWired()) {


                        } else if (ActionParameterType.WIRED_VALUE.equals(e.getParameterType())) {
                            value = ConvertUtils.convert(e.getReferredTo(), field.getType());

                        } else if (ActionParameterType.WIRED_PROPERTY.equals(e.getParameterType())) {
                            value = ConvertUtils.convert(actionContext.getProperty(e.getReferredTo(), e.isRequired()), field.getType());

                        } else if (ActionParameterType.WIRED_SERVICE.equals(e.getParameterType())) {
                            try {
                                // FIXME:
                                value = actionContext.getService(e.getReferredTo(), field.getType());
                            } catch (NotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else if (ActionParameterType.WIRED_RESOURCE.equals(e.getParameterType())) {
                            // FIXME:
                            value = actionContext.getResource(e.getReferredTo(), field.getType());

                        }

                        if (value != null) {
                            field.setAccessible(true);
                            try {
                                field.set(callable, value);
                            } catch (IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }

                return callable;

            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
