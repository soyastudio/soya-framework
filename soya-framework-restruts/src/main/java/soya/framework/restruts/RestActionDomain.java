package soya.framework.restruts;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestActionDomain {
    String path() default "/";

    RestAction[] actions();
}
