package soya.framework.action.orchestration;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionName;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionProperty;
import soya.framework.commons.util.DefaultUtils;
import soya.framework.commons.util.ReflectUtils;

import java.util.Arrays;

public abstract class AnnotatedDynaAction<T> extends DynaActionBase<T> {

    protected ActionName actionName;

    public AnnotatedDynaAction() {
        ActionDefinition annotation = getClass().getAnnotation(ActionDefinition.class);
        this.actionName = ActionName.create(annotation.domain(), annotation.name());
        Arrays.stream(annotation.properties()).forEach(p -> {
            ActionProperty property = ActionProperty.builder()
                    .name(p.name())
                    .type(DefaultUtils.isDefaultType(p.type())? Object.class : p.type())
                    .propertyType(p.propertyType())
                    .referredTo(p.referredTo())
                    .required(p.required())
                    .description(p.description())
                    .create();
            parameters.put(property.getName(), new ActionParameter(property));
        });
    }

    protected Class<?> getReturnType() {
        return ReflectUtils.findMethod(getClass(), "call", new Class<?>[0]).getReturnType();
    }
}
