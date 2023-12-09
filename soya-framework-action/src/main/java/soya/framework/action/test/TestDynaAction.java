package soya.framework.action.test;

import soya.framework.action.*;
import soya.framework.action.orchestration.WorkflowAction;
import soya.framework.action.orchestration.WorkflowBuilder;

@ActionDefinition(
        domain = "test",
        name = "dynaActionTest",
        properties = {
                @ActionPropertyDefinition(name = "value",
                        propertyType = ActionPropertyType.WIRED_RESOURCE,
                        referredTo = "classpath:banner.txt")
        }
)
public class TestDynaAction extends WorkflowAction<String> {

    @Override
    protected WorkflowBuilder builder() {
        return null;
    }

    @Override
    protected void build(WorkflowBuilder builder) {

    }
}
