package soya.framework.action.actions.dispatch;

import soya.framework.commons.util.DefaultUtils;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyMapping {
    String name() default "";

    Class<?> type() default DefaultUtils.DEFAULT_TYPE.class;

    String actionProperty();

}
