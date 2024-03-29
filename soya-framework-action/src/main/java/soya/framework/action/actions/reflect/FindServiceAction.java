package soya.framework.action.actions.reflect;

import com.google.gson.GsonBuilder;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "service"
)
public class FindServiceAction implements Callable<String> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM,
            required = true)
    private String type;

    @Override
    public String call() throws Exception {
        Map<String, String> results = new LinkedHashMap<>();
        Class<?> cls = Class.forName(type);
        ServiceLocatorSingleton.getInstance().getServices(cls).entrySet().forEach(e -> {
            results.put(e.getKey(), e.getValue().getClass().getName());
        });
        return new GsonBuilder().setPrettyPrinting().create().toJson(results);
    }
}
