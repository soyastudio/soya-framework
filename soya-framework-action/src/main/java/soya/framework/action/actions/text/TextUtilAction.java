package soya.framework.action.actions.text;

import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public abstract class TextUtilAction implements Callable<String> {

    protected String encoding = Charset.defaultCharset().toString();

    protected String text;

    public String call() throws Exception {
        return execute();
    }

    protected abstract String execute() throws Exception;

}
