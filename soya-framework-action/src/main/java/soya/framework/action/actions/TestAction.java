package soya.framework.action.actions;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;
import soya.framework.commons.io.Resource;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "test",
        name = "field-annotation",
        parameters = {
                @ActionParameter(name = "value",
                        type = ActionParameterType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestAction implements Callable<Object> {

    @ActionParameter(type = ActionParameterType.PROPERTY)
    private String property;

    @ActionParameter(type = ActionParameterType.PROPERTY)
    private String value;

    @ActionParameter(type = ActionParameterType.INPUT)
    private Object input;

    @ActionParameter(type = ActionParameterType.WIRED_SERVICE,
            referredTo = "actionContext")
    private ActionContext service;

    @ActionParameter(type = ActionParameterType.WIRED_RESOURCE,
            referredTo = "invoke:java.util.Date")
            //referredTo = "invoke:soya.framework.commons.util.DateTimeUtils/current")
    private Resource resource;

    @ActionParameter(type = ActionParameterType.WIRED_RESOURCE,
            referredTo = "classpath:banner.txt")
    //referredTo = "invoke:soya.framework.commons.util.DateTimeUtils/current")
    private String banner;

    @ActionParameter(type = ActionParameterType.WIRED_PROPERTY,
            referredTo = "server.port")
    private Object configuration;

    @Override
    public final Object call() throws Exception {
        return banner;
    }
}
