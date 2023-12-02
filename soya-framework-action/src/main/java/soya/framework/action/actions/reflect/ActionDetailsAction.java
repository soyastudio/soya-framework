package soya.framework.action.actions.reflect;

import com.google.gson.GsonBuilder;
import soya.framework.action.*;

import java.net.URI;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "action-details"
)
public class ActionDetailsAction implements Callable<String> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE,
            required = true)
    private String uri;

    @Override
    public String call() throws Exception {
        ActionClass actionClass = ActionClass.forName(ActionName.fromURI(new URI(uri)));
        return new GsonBuilder().setPrettyPrinting().create().toJson(actionClass);
    }
}
