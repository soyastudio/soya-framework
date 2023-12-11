package soya.framework.action.orchestration;

public abstract class PipelineAction<T> extends WorkflowAction<T> {

    public PipelineAction() {
        super();
    }

    @Override
    protected WorkflowBuilder builder() {
        return new PipelineBuilder();
    }

    @Override
    protected void build(WorkflowBuilder builder) {
        build((PipelineBuilder) builder);
    }
    protected abstract void build(PipelineBuilder builder);
}
