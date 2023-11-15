package soya.framework.restruts;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestActionParameter {
    String name();

    ParamType paramType();

    String referredTo() default "";

    boolean required() default false;

    String description() default "";

}
