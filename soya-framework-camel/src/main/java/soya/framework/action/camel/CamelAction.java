package soya.framework.action.camel;

import soya.framework.action.orchestration.WorkflowAction;
import soya.framework.action.orchestration.WorkflowBuilder;

public abstract class CamelAction<T> extends WorkflowAction<T> {
    public CamelAction() {
        super();
    }

    @Override
    protected WorkflowBuilder builder() {
        return null;
    }

    @Override
    protected void build(WorkflowBuilder builder) {

    }
}
