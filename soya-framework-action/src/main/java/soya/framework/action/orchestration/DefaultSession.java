package soya.framework.action.orchestration;

import soya.framework.action.ActionName;
import soya.framework.action.ActionParameter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class DefaultSession implements Session {

    private final ActionName actionName;
    private final String id;
    private final long startTime;
    protected final Map<String, ActionParameter> parameters;
    private Map<String, Object> attributes = new HashMap<>();

    DefaultSession(ActionName actionName, Map<String, ActionParameter> parameters) {
        this.actionName = actionName;
        this.id = UUID.randomUUID().toString();
        this.startTime = System.currentTimeMillis();
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    @Override
    public ActionName getActionName() {
        return actionName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public Object getParameter(String name) {
        if (!parameters.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined: " + name);
        }

        return parameters.get(name).get();
    }

    @Override
    public Object get(String attrName) {
        return attributes.get(attrName);
    }

    @Override
    public void set(String attrName, Object attrValue) {
        if (attrValue == null) {
            attributes.remove(attrName);
        } else {
            attributes.put(attrName, attrValue);
        }
    }

    @Override
    public Object evaluate(String exp) {
        // FIXME:
        if(exp.startsWith("{") && exp.endsWith("}")) {
            String str = exp.substring(1, exp.length() - 1);
            return attributes.get(str);

        } else {
            return parameters.get(exp).get();

        }
    }

}
