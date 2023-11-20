package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Contact {
    String name() default "";

   String url() default "";

    String email() default "";

    Extension[] extensions() default {};

}
