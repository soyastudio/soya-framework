package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.commons.io.Resource;
import soya.framework.context.ServiceLocatorSingleton;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "resource"
)
public class ResourceAction implements Callable<String> {

    @ActionParameterDefinition(
            type = ActionParameterType.PROPERTY,
            required = true
    )
    private String uri;

    @Override
    public String call() throws Exception {
        Resource resource = ServiceLocatorSingleton.getInstance().getResource(new URI(uri));
        return resource.getAsString(Charset.defaultCharset());
    }
}
