package soya.framework.action.orchestration.annotation;

import soya.framework.action.orchestration.Mocking.*;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MockActionDefinition {

    LogLevel logLevel() default LogLevel.WARNING;

    String message() default "";

    MockStrategy mockStrategy() default MockStrategy.DEFAULT;

    String mockResult() default "";

}
