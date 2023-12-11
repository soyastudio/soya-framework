package soya.framework.action.orchestration;

import soya.framework.action.ActionName;

import java.util.HashMap;
import java.util.Map;

public final class WorkflowRegistration {
    private static WorkflowRegistration me;

    private Map<ActionName, Workflow> registration = new HashMap<>();

    static {
        me = new WorkflowRegistration();
    }

    private WorkflowRegistration() {
    }

    public void register(ActionName actionName, Workflow flow) {
        if(!registration.containsKey(actionName)) {
            registration.put(actionName, flow);
        }
    }

    public boolean contains(ActionName actionName) {
        return registration.containsKey(actionName);
    }

    public Workflow get(ActionName actionName) {
        return registration.get(actionName);
    }

    public static WorkflowRegistration getInstance() {
        return me;
    }
}
