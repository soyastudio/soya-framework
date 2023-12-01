package soya.framework.action.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.*;
import soya.framework.restruts.ActionMapping;
import soya.framework.restruts.DispatchAction;
import soya.framework.restruts.ParamType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public final class ActionDispatchAction extends DispatchAction<String> {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ActionClass actionClass;

    public ActionDispatchAction(ActionMapping mapping) {
        super(mapping);
        try {
            actionClass = ActionClass.forName(ActionName.fromURI(new URI(mapping.getAction())));
            Arrays.stream(mapping.getParameters()).forEach(p -> {
                if(!p.getParameterType().equals(ParamType.WIRED_PROPERTY)
                || !p.getParameterType().equals(ParamType.WIRED_SERVICE)
                || !p.getParameterType().equals(ParamType.WIRED_RESOURCE)) {

                    properties.put(p.getName(), new DynaProperty(p.getName(), String.class));

                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String call() throws Exception {
        ActionExecutor actionExecutor = actionClass.executor(actionContext());
        Arrays.stream(getPropertyNames()).forEach(pn -> {
            String value = (String) getPropertyValue(pn);
            if(value != null && value.trim().length() > 0) {
                actionExecutor.set(pn, value);
            }
        });

        Object result = actionExecutor.execute();

        if(result instanceof String) {
            return (String) result;
        } else {
            return GSON.toJson(result);
        }
    }

    private ActionContext actionContext() {
        return getRestActionContext().getService(null, ActionContext.class);
    }
}
