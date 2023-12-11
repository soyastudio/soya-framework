package soya.framework.action.camel.admin;

import soya.framework.action.ActionDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "camel-admin",
        name = "components"
)
public class ComponentsAction extends CamelAdminAction<String[]>{
    @Override
    public String[] call() throws Exception {
        List<String> list = new ArrayList<>(getCamelContext().getComponentNames());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }
}
