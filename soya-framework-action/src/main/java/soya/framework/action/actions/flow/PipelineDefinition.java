package soya.framework.action.actions.flow;

import soya.framework.action.ActionPropertyDefinition;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelineDefinition {

    String domain();

    String name();

    String description() default "";

    ActionPropertyDefinition[] parameters() default {};

    TaskDefinition[] tasks();

}
