package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Server {
    String url() default "";

    Extension[] extensions() default {};
}
