package soya.framework.restruts.oas;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface API {
    String openapi() default "3.0.0";

    Info info() default @Info;

    Server[] servers();



}
