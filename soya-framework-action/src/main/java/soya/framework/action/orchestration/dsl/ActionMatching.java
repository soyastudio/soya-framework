package soya.framework.action.orchestration.dsl;

public @interface ActionMatching {

    String[] activity();

    String[] expressions() default {};

    String[] keywords() default {};
    
}
