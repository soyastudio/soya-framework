package soya.framework.action.camel;

import soya.framework.action.orchestration.annotation.TaskDefinition;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CamelRouteDefinition {
    TaskDefinition from();

    TaskDefinition[] to() default {};

}
