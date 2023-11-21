package soya.framework.action.actions.reflect;

import soya.framework.action.ActionDefinition;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "services"
)
public class ServiceNamesAction implements Callable<String[]> {
    @Override
    public String[] call() throws Exception {
        return ServiceLocatorSingleton.getInstance().serviceNames();
    }
}
