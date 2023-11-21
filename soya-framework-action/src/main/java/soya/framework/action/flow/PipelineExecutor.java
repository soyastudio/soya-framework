package soya.framework.action.flow;

import soya.framework.action.ActionContext;

public class PipelineExecutor {
    private final Pipeline pipeline;
    private final ActionContext actionContext;

    PipelineExecutor(Pipeline pipeline, ActionContext actionContext) {
        this.pipeline = pipeline;
        this.actionContext = actionContext;
    }
}
