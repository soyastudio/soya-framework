package soya.framework.restruts;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestActionParameter {
    String name();

    ParamType paramType();

    String referredTo() default "";

    String description() default "";

}
