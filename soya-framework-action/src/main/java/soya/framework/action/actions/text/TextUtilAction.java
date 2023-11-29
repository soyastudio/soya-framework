package soya.framework.action.actions.text;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import java.nio.charset.Charset;
import java.util.concurrent.Callable;

public abstract class TextUtilAction implements Callable<String> {
    public static final Charset DEFAULT_ENCODING = Charset.defaultCharset();

    @ActionParameterDefinition(
            parameterType = ActionParameterType.ATTRIBUTE
    )
    protected String encoding;

    @ActionParameterDefinition(
            parameterType = ActionParameterType.INPUT
    )
    protected String text;

    public String call() throws Exception {
        return execute();
    }

    protected abstract String execute() throws Exception;

    protected final String getEncoding() {
        return encoding == null || encoding.trim().isEmpty() ? DEFAULT_ENCODING.toString() : encoding;
    }


}
