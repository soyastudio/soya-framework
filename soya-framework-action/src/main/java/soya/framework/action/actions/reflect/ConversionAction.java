package soya.framework.action.actions.reflect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.commons.conversion.ConvertUtils;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "conversion"
)
public class ConversionAction implements Callable<Object> {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE, required = true)
    private String destType;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE, required = true)
    private String valueType;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT, required = true)
    private String value;

    @Override
    public Object call() throws Exception {

        Class<?> destClass = Class.forName(destType);

        // FIXME:
        Class<?> valueClass = Class.forName(valueType);
        Object v = GSON.fromJson(value, valueClass);

        return ConvertUtils.convert(v, Class.forName(valueType));
    }
}
