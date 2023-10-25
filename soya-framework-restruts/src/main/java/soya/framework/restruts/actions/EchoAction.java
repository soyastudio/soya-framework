package soya.framework.restruts.actions;

import java.util.concurrent.Callable;

public class EchoAction implements Callable<String> {

    private String message;
    @Override
    public String call() throws Exception {
        return message;
    }
}
