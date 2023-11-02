package soya.framework.restruts;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestActionDomain {
    String domain();
    RestAction[] actions() default {};
}
