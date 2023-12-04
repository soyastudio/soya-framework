package soya.framework.action.actions.dispatch;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DispatchActionParameter {
    Class<?> type();

    String actionParameter();

}
