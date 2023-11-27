package soya.framework.action.flow.samples;

import soya.framework.action.flow.Pipeline;
import soya.framework.action.flow.PipelineAction;

public class TestPipeline extends PipelineAction<String> {

    public TestPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public Object getParameter(String paramName) {
        return null;
    }
}
