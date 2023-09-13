package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Domain {
    String name();

    String path();

    String title() default "";

    String description() default "";
}
