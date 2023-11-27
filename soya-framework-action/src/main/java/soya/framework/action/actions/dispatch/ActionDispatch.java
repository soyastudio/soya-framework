package soya.framework.action.actions.dispatch;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionDispatch {
    String action();


}
