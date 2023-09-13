package soya.framework.container;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Container {
    Class<?>[] componentTypes();
}
