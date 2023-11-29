package soya.framework.action.actions.flow;

import soya.framework.action.ActionParameterDefinition;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelineDefinition {

    String domain();

    String name();

    String description() default "";

    ActionParameterDefinition[] parameters() default {};

    TaskDefinition[] tasks();

}
