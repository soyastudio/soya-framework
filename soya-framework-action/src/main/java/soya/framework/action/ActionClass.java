package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public final class ActionClass implements Serializable {

    private static Map<String, ActionDomain> DOMAINS = new HashMap<>();
    private static Map<ActionName, ActionClass> ACTION_CLASSES = new HashMap<>();
    private static Map<Class<? extends ActionCallable>, ActionClass> ACTION_TYPES = new HashMap<>();

    private static Registry registry = new Registry();

    private final transient Class<? extends ActionCallable> actionType;
    private transient List<Field> wiredFields = new ArrayList<>();

    private transient Map<String, Field> actionFields = new LinkedHashMap<>();
    private transient Map<String, Field> options = new LinkedHashMap<>();

    private final ActionName actionName;
    private final String resultFormat;

    ActionClass(Class<? extends ActionCallable> actionType) {

        ActionDefinition mapping = actionType.getAnnotation(ActionDefinition.class);
        Objects.requireNonNull(mapping, "Class is not annotated as 'OperationMapping': " + actionType.getName());

        ActionName actionName = ActionName.create(mapping.domain(), mapping.name());

        this.actionName = actionName;
        this.actionType = actionType;

        for (Field field : findActionFields()) {
            if (field.getAnnotation(ActionProperty.class) != null) {
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                actionFields.put(field.getName(), field);
                if (!actionProperty.option().isEmpty()) {
                    options.put(actionProperty.option(), field);
                }

            } else if (field.getAnnotation(WiredService.class) != null
                    || field.getAnnotation(WiredProperty.class) != null
                    || field.getAnnotation(WiredResource.class) != null) {
                wiredFields.add(field);
            }
        }

        this.resultFormat = mapping.produces()[0];

        // ------------
        ACTION_CLASSES.put(actionName, this);
        ACTION_TYPES.put(actionType, this);

    }

    public ActionName getActionName() {
        return actionName;
    }

    public Class<? extends ActionCallable> getActionType() {
        return actionType;
    }

    public Field[] getActionFields() {
        return actionFields.values().toArray(new Field[actionFields.size()]);
    }

    public Field getActionField(String name) {
        if (name.length() == 1) {
            return options.get(name);
        } else {
            return actionFields.get(name);
        }
    }

    public String getResultFormat() {
        return resultFormat;
    }

    public String toURI() {
        StringBuilder builder = new StringBuilder(actionName.toString());
        if (actionFields.size() > 0) {
            builder.append("?");
            actionFields.entrySet().forEach(e -> {
                builder.append(e.getKey()).append("=assign(").append(e.getKey()).append(")").append("&");
            });

            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public ActionCallable newInstance() throws ActionCreationException {

        try {
            ActionCallable action = actionType.newInstance();

            wiredFields.forEach(e -> {
                Class<?> type = e.getType();
                Object value = null;
                if (e.getAnnotation(WiredService.class) != null) {
                    WiredService wiredService = e.getAnnotation(WiredService.class);
                    value = wiredService.name().isEmpty() ? ActionContext.getInstance().getService(type)
                            : ActionContext.getInstance().getService(wiredService.name(), type);

                } else if (e.getAnnotation(WiredProperty.class) != null) {
                    String prop = ActionContext.getInstance().getProperty(e.getAnnotation(WiredProperty.class).value());
                    value = ConvertUtils.convert(prop, type);

                } else if (e.getAnnotation(WiredResource.class) != null) {
                    String res = Resources.getResourceAsString(e.getAnnotation(WiredResource.class).value());
                    value = ConvertUtils.convert(res, type);

                }

                e.setAccessible(true);
                try {
                    e.set(action, value);
                } catch (IllegalAccessException ex) {
                    throw new ActionCreationException(ex);
                }

            });

            actionFields.values().forEach(field -> {
                ActionProperty property = field.getAnnotation(ActionProperty.class);
                if (property.required() && !property.defaultValue().isEmpty()) {
                    Object value = ConvertUtils.convert(property.defaultValue(), field.getType());

                    if (value != null) {
                        field.setAccessible(true);
                        try {
                            field.set(action, value);
                        } catch (IllegalAccessException e) {
                            throw new ActionCreationException(e);
                        }
                    }
                }
            });

            return action;

        } catch (Exception e) {
            throw new ActionCreationException(e);
        }
    }

    private Field[] findActionFields() {
        List<Field> fields = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        Class<?> cls = actionType;
        while (!cls.getName().equals("java.lang.Object")) {
            for (Field field : cls.getDeclaredFields()) {
                if (field.getAnnotation(ActionProperty.class) != null
                        && !fieldNames.contains(field.getName())) {

                    fields.add(field);
                    fieldNames.add(field.getName());

                } else if (field.getAnnotation(WiredService.class) != null
                        || field.getAnnotation(WiredProperty.class) != null
                        || field.getAnnotation(WiredResource.class) != null) {

                    fields.add(field);

                }
            }

            cls = cls.getSuperclass();
        }

        Collections.sort(fields, new ParameterFieldComparator());

        return fields.toArray(new Field[fields.size()]);
    }

    static ActionRegistry registry() {
        return registry;
    }

    public static ActionDomain createActionDomain(Class<?> cls) {
        Domain domain = cls.getAnnotation(Domain.class);
        if (domain == null) {
            throw new IllegalArgumentException("Class '" + cls.getName() + "' is not annotated as Domain.");
        }

        if (DOMAINS.containsKey(domain.name())) {
            throw new IllegalArgumentException("Domain '" + domain.name() + "' already exists.");
        }

        DOMAINS.put(domain.name(), ActionDomain.builder().fromAnnotation(domain).create());

        return DOMAINS.get(domain.name());
    }

    public static ActionClass get(ActionName actionName) {
        return ACTION_CLASSES.get(actionName);
    }

    public static ActionClass get(Class<? extends ActionCallable> actionType) {
        if (!ACTION_TYPES.containsKey(actionType)) {
            new ActionClass(actionType);
        }

        return ACTION_TYPES.get(actionType);
    }

    private final class ParameterFieldComparator implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
            if (o1.getAnnotation(ActionProperty.class) != null && o2.getAnnotation(ActionProperty.class) != null) {
                ActionProperty a1 = o1.getAnnotation(ActionProperty.class);
                ActionProperty a2 = o2.getAnnotation(ActionProperty.class);

                int result = ActionParameterType.index(a1.parameterType())
                        - ActionParameterType.index(a2.parameterType());
                if (result != 0) {
                    return result;
                }

                result = a1.displayOrder() - a2.displayOrder();
                if (result != 0) {
                    return result;
                }
            }

            return o1.getName().compareTo(o2.getName());
        }
    }

    private static final class Registry implements ActionRegistry, ActionFactory {
        private static final String ID = "ACTION_CLASS_REGISTRY";
        private long lastUpdatedTime;

        private Registry() {
            this.lastUpdatedTime = System.currentTimeMillis();
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public long lastUpdatedTime() {
            return lastUpdatedTime;
        }

        @Override
        public Collection<ActionDomain> domains() {
            return DOMAINS.values();
        }

        @Override
        public Collection<ActionDescription> actions() {
            Set<ActionDescription> set = new HashSet<>();
            ACTION_CLASSES.entrySet().forEach(e -> {
                set.add(ActionDescription.builder().fromActionClass(e.getValue()).create());
            });
            return set;
        }

        @Override
        public ActionFactory actionFactory() {
            return this;
        }

        @Override
        public ActionBean create(ActionName actionName) {
            ActionClass actionClass = ACTION_CLASSES.get(actionName);
            return new ActionBean(actionClass.newInstance(), ActionDescription.builder().fromActionClass(actionClass).create());
        }
    }
}
