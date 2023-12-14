package soya.framework.action.orchestration.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MockActionDefinition {
    LogLevel logLevel() default LogLevel.WARNING;

    String message() default "";

    String mockResult() default "";

    enum LogLevel {
        ERROR, WARNING, INFO, DEBUG
    }
}
