package soya.framework.action.test;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.orchestration.AnnotatedDynaAction;
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
public class TestDynaAction extends AnnotatedDynaAction<String> {
        @Override
        public String call() throws Exception {
                return null;
        }

    /*@Override
    protected WorkflowBuilder builder() {
        return null;
    }

    @Override
    protected void build(WorkflowBuilder builder) {

    }*/
}
