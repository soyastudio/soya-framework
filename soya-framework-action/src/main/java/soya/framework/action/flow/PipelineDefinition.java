package soya.framework.action.flow;

import soya.framework.action.ActionParameter;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelineDefinition {
    String domain();

    String name();

    String description() default "";

    ActionParameter[] parameters() default {};

    TaskDefinition[] tasks();


}
