package soya.framework.action.orchestration;

import soya.framework.commons.util.DefaultUtils;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParameterMapping {
    String name() default "";

    Class<?> type() default DefaultUtils.DEFAULT_TYPE.class;

    String mappingTo();

}
