package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionParameter {
    String name() default "";

    ActionParameterType type();

    String referredTo() default "";

    int order() default 5;

    boolean required() default false;

    String description() default "";

}
