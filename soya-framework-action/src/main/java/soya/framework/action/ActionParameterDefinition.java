package soya.framework.action;

import soya.framework.commons.util.DefaultUtils;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionParameterDefinition {

    String name() default "";

    Class<?> type() default DefaultUtils.DEFAULT_TYPE.class;

    ActionParameterType parameterType();

    String referredTo() default "";

    int order() default 5;

    boolean required() default false;

    String description() default "";

}
