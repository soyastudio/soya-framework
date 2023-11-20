package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface License {
    String name() default "";

    String url() default "";

    Extension[] extensions() default {};

}
