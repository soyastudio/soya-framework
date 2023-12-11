package soya.framework.action.orchestration.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipelineDefinition {
    TaskDefinition[] tasks();

}
