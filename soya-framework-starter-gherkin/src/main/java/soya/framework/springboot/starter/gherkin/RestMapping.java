package soya.framework.springboot.starter.gherkin;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestMapping {
    HttpMethod method() default HttpMethod.GET;

    String path();

    @interface ParamMapping {
        String name();

    }

    enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    enum ParamType {
        HEAD, QUERY_STRING, PATH, PAYLOAD
    }
}
