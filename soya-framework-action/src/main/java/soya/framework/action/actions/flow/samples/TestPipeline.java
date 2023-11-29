package soya.framework.action.actions.flow.samples;

import soya.framework.action.actions.flow.Pipeline;
import soya.framework.action.actions.flow.PipelineAction;

public class TestPipeline extends PipelineAction<String> {

    public TestPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public Object getParameter(String paramName) {
        return null;
    }
}
