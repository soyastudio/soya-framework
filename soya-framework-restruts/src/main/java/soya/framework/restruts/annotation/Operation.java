package soya.framework.restruts.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Operation {
    String id() default "";

    String summary() default "";

    String description() default "";


}
