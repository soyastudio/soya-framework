package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Operation {
    String path();

    String method();

    String id() default "";

    String description() default "";

    Parameter[] parameters() default {};

    Extension[] extensions() default {};
}
