package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Info {
    String title() default "";

    String description() default "";

    String termsOfService() default "";

    Contact contact() default @Contact();

    License license() default @License();

    String version() default "";

    Extension[] extensions() default {};
}
