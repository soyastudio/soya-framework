package soya.framework.action.orchestration.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskDefinition {
    String name() default "";

    String uri();

    ParameterMapping[] parameterMappings() default {};

}
