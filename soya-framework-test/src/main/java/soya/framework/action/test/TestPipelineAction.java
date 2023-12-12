package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.orchestration.PipelineAction;
import soya.framework.action.orchestration.PipelineBuilder;
import soya.framework.action.orchestration.Processor;
import soya.framework.action.orchestration.Session;

@ActionDefinition(
        domain = "test",
        name = "pipeline-test"
)
public class TestPipelineAction extends PipelineAction<String> {
    @Override
    protected void build(PipelineBuilder builder) {
        builder.addProcessor(session -> "Test Pipeline with Processor");
    }
}
