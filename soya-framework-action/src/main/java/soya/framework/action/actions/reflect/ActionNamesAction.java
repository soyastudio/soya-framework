package soya.framework.action.actions.reflect;

import soya.framework.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "actionNames"
)
public class ActionNamesAction implements Callable<ActionName[]> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    private String domain;

    @Override
    public ActionName[] call() throws Exception {
        if(domain != null) {
            List<ActionName> list = new ArrayList<>();
            Arrays.stream(ActionClass.actionNames()).forEach(e -> {
                if(domain.equals(e.getDomain())) {
                    list.add(e);
                }
            });
            return list.toArray(new ActionName[list.size()]);

        } else {
            return ActionClass.actionNames();
        }
    }
}
