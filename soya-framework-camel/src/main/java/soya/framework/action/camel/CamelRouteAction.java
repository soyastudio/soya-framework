package soya.framework.action.camel;

import soya.framework.action.orchestration.WorkflowAction;
import soya.framework.action.orchestration.WorkflowBuilder;

public abstract class CamelRouteAction<T> extends WorkflowAction<T> {
    public CamelRouteAction() {
        super();
    }

    @Override
    protected WorkflowBuilder builder() {
        return new CamelRouteFlowBuilder(actionName.toString());
    }

    @Override
    protected void build(WorkflowBuilder builder) {
        build(((CamelRouteFlowBuilder) builder));
    }

    protected abstract void build(CamelRouteFlowBuilder builder);
}
