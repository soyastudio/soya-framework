package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.orchestration.AnnotatedPipelineAction;
import soya.framework.action.orchestration.annotation.ParameterMapping;
import soya.framework.action.orchestration.annotation.PipelineDefinition;
import soya.framework.action.orchestration.annotation.TaskDefinition;

@ActionDefinition(
        domain = "test",
        name = "pipelineActionTest",
        properties = {
                @ActionPropertyDefinition(
                        name = "message",
                        propertyType = ActionPropertyType.INPUT)
        }
)
@PipelineDefinition(
        tasks = {
                @TaskDefinition(
                        name = "encoded",
                        uri = "text-utils://base64encode?text=message"
                ),
                @TaskDefinition(
                        uri = "text-utils://base64decode",
                        parameterMappings = {
                                @ParameterMapping(
                                        name = "text",
                                        mappingTo = "{encoded}"
                                )
                        }
                )
        }
)
public class TestPipelineAction extends AnnotatedPipelineAction<String> {
}
