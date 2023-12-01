package soya.framework.action.actions.reflect;

import soya.framework.action.ActionClass;
import soya.framework.action.ActionDefinition;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "reflect",
        name = "domains"
)
public class ActionDomainsAction implements Callable<String[]> {
    @Override
    public String[] call() throws Exception {
        Set<String> set = new LinkedHashSet<>();
        Arrays.stream(ActionClass.actionNames()).forEach(e -> {
            set.add(e.getDomain());
        });

        return set.toArray(new String[set.size()]);
    }
}
