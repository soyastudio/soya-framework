package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionProperty {

    String name() default "";

    String option() default "";

    int displayOrder() default 5;

    String[] description() default {};

    boolean required() default false;

    String defaultValue() default "";

    ActionParameterType parameterType();

    String contentType() default MediaType.TEXT_PLAIN;



}
