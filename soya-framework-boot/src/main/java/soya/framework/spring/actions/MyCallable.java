package soya.framework.spring.actions;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {

    private String message;
    @Override
    public String call() throws Exception {
        return message;
    }
}
