package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;
import soya.framework.context.ServiceLocatorSingleton;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "resource"
)
public class ResourceAction implements Callable<String> {

    @ActionParameter(
            type = ActionParameterType.PROPERTY
    )
    private String uri;

    @Override
    public String call() throws Exception {
        return ServiceLocatorSingleton.getInstance().getResource(new URI(uri)).getAsString(Charset.defaultCharset());
    }
}
