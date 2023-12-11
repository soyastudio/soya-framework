package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.orchestration.PipelineAction;
import soya.framework.action.orchestration.PipelineBuilder;
import soya.framework.action.orchestration.Task;

@ActionDefinition(
        domain = "test",
        name = "pipelineActionTest",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_RESOURCE,
                        referredTo = "classpath:banner.txt")
        }
)
public class TestPipelineAction extends PipelineAction<String> {

    public TestPipelineAction() {
        super();
    }

    @Override
    protected void build(PipelineBuilder builder) {
        builder.addTask(Task.builder().name("A"))
                .addTask(Task.builder().name("B"));

    }
}
