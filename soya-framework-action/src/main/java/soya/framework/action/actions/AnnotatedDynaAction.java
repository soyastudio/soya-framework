package soya.framework.action.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionProperty;
import soya.framework.commons.util.DefaultUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AnnotatedDynaAction<T> extends DynaActionBase<T> {

    public AnnotatedDynaAction() {
        Arrays.stream(fromAnnotation()).forEach(p -> {
            parameters.put(p.getName(), new ActionParameter(p));
        });
    }

    protected ActionProperty[] fromAnnotation() {
        List<ActionProperty> list = new ArrayList<>();
        ActionDefinition annotation = getClass().getAnnotation(ActionDefinition.class);
        Arrays.stream(annotation.properties()).forEach(p -> {
            list.add(ActionProperty.builder()
                    .name(p.name())
                    .type(DefaultUtils.isDefaultType(p.type())? Object.class : p.type())
                    .parameterType(p.propertyType())
                    .referredTo(p.referredTo())
                    .required(p.required())
                    .description(p.description())
                    .create());
        });
        return list.toArray(new ActionProperty[list.size()]);
    }
}
