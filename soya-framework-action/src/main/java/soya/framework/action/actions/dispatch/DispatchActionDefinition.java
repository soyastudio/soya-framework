package soya.framework.action.actions.dispatch;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DispatchActionDefinition {

    String uri();

    DispatchActionParameter[] parameters() default {};

}
