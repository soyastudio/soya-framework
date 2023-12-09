package soya.framework.action.orchestration;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DispatchDefinition {

    String uri();

    ParameterMapping[] parameterMappings() default {};

}
