package soya.framework.action;

import java.io.Serializable;
import java.lang.reflect.Field;

public final class ActionPropertyDescription implements Comparable<ActionPropertyDescription>, Serializable {

    private final String name;
    private final Class<?> propertyType;

    private final String option;
    private final String[] description;
    private final boolean required;
    private final int displayOrder;
    private final ActionParameterType parameterType;
    private final String contentType;

    private ActionPropertyDescription(String name, Class<?> propertyType, String option, String[] description, boolean required, int displayOrder, ActionParameterType parameterType, String contentType) {
        this.name = name;
        this.propertyType = propertyType;

        this.option = option;
        this.description = description;
        this.required = required;
        this.displayOrder = displayOrder;
        this.parameterType = parameterType;
        this.contentType = contentType;
    }

    public static ActionPropertyDescription create(String name, Class<?> type, ActionProperty annotation) {
        return new ActionPropertyDescription(
                name, type,
                annotation.option(),
                annotation.description(),
                annotation.required(),
                annotation.displayOrder(),
                annotation.parameterType(),
                annotation.contentType());
    }

    public static ActionPropertyDescription fromActionField(Field field) {
        return create(field.getName(), field.getType(), field.getAnnotation(ActionProperty.class));
    }

    public String getName() {
        return name;
    }

    public String getOption() {
        return option;
    }

    public String[] getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public ActionParameterType getParameterType() {
        return parameterType;
    }

    public String getContentType() {
        return contentType;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    @Override
    public int compareTo(ActionPropertyDescription o) {
        int result = ActionParameterType.index(parameterType) - ActionParameterType.index(o.parameterType);
        if(result == 0) {
            result = displayOrder - o.displayOrder;
        }

        if(result == 0) {
            result = name.compareTo(o.name);
        }

        return result;
    }
}
