package soya.framework.commons.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Todo {
    TODO_TYPE type() default TODO_TYPE.NOT_IMPLEMENTED;

    String message() default "";

    enum TODO_TYPE {
        NOT_IMPLEMENTED, FIXME, TO_BE_DEPRECATED
    }
}
