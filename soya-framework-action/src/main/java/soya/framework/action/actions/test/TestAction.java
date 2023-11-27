package soya.framework.action.actions.test;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.commons.io.Resource;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "test",
        name = "field-annotation",
        parameters = {
                @ActionParameterDefinition(name = "value",
                        type = ActionParameterType.WIRED_VALUE,
                        referredTo = "xyz")
        }
)
public class TestAction implements Callable<Object> {

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    private String property;

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    private String value;

    @ActionParameterDefinition(type = ActionParameterType.INPUT)
    private Object input;

    @ActionParameterDefinition(type = ActionParameterType.WIRED_SERVICE,
            referredTo = "actionContext")
    private ActionContext service;

    @ActionParameterDefinition(type = ActionParameterType.WIRED_RESOURCE,
            referredTo = "invoke:java.util.Date")
            //referredTo = "invoke:soya.framework.commons.util.DateTimeUtils/current")
    private Resource resource;

    @ActionParameterDefinition(type = ActionParameterType.WIRED_RESOURCE,
            referredTo = "classpath:banner.txt")
    //referredTo = "invoke:soya.framework.commons.util.DateTimeUtils/current")
    private String banner;

    @ActionParameterDefinition(type = ActionParameterType.WIRED_PROPERTY,
            referredTo = "server.port")
    private Object configuration;

    @Override
    public final Object call() throws Exception {
        return banner;
    }
}
