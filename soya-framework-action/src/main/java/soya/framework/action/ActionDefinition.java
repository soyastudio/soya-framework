package soya.framework.action;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionDefinition {
    String domain();

    String name();

    String description() default "";

    ActionPropertyDefinition[] properties() default {};

}
