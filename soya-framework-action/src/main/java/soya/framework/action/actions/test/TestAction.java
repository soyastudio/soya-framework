package soya.framework.action.actions.test;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.commons.io.Resource;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "test",
        name = "field-annotation",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestAction implements Callable<Object> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE)
    private String property;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE)
    private String value;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT)
    private Object input;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.WIRED_SERVICE,
            referredTo = "actionContext")
    private ActionContext service;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.WIRED_RESOURCE,
            referredTo = "invoke:java.util.Date")
            //referredTo = "invoke:soya.framework.commons.util.DateTimeUtils/current")
    private Resource resource;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.WIRED_RESOURCE,
            referredTo = "classpath:banner.txt")
    //referredTo = "invoke:soya.framework.commons.util.DateTimeUtils/current")
    private String banner;

    @ActionPropertyDefinition(propertyType = ActionPropertyType.WIRED_PROPERTY,
            referredTo = "server.port")
    private Object configuration;

    @Override
    public final Object call() throws Exception {
        return banner;
    }
}
