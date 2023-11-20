package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Parameter {
    String name();

    String in();

    boolean required() default false;


}
