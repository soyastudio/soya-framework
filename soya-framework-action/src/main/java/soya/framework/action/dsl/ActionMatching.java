package soya.framework.action.dsl;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMatching {

    String activity();

    String preposition() default "";

    String[] objects();

    String[] complements() default {};
    
}
