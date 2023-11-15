package soya.framework.restruts.annotation;

import soya.framework.restruts.ParamType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String name() default "";

    ParamType type();

    String description() default "";

    boolean required() default false;

    boolean deprecated() default false;
}
