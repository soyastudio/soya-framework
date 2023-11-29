package soya.framework.action;

import soya.framework.commons.util.DefaultUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ActionClassBuilder {

    private Class<? extends Callable> actionType;
    private ActionName actionName;
    private List<ActionProperty> properties = new ArrayList<>();

    public ActionClassBuilder actionType(Class<? extends Callable> type) {
        this.actionType = type;
        return this;
    }

    public ActionClassBuilder actionName(ActionName actionName) {
        this.actionName = actionName;
        return this;
    }

    public ActionClassBuilder addProperty(ActionProperty property) {
        this.properties.add(property);
        return this;
    }

    public ActionClassBuilder addProperty(Field field, ActionParameterDefinition annotation) {
        this.properties.add(ActionProperty.builder()
                .name(field.getName())
                .type(DefaultUtils.isDefaultType(annotation.type())? field.getType() : DefaultUtils.getDefaultType(field.getType()))
                .parameterType(annotation.parameterType())
                .referredTo(annotation.referredTo())
                .required(annotation.required())
                .description(annotation.description())
                .create());
        return this;
    }

    public ActionClass create(boolean register) {
        ActionClass actionClass = new ActionClass(actionType, actionName, properties);
        if (register) {
            ActionClass.register(actionClass);
        }

        return actionClass;
    }

}
