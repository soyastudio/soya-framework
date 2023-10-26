package soya.framework.restruts;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestActionMapping {
    String action() default "";

    HttpMethod method();

    String path();

    RestActionParameter[] parameters() default {};

    String[] consumes() default "text/plain";

    String[] produces() default "text/plain";


}
