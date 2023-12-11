package soya.framework.action.orchestration;

import soya.framework.action.ActionName;
import soya.framework.commons.conversion.ConvertUtils;

public abstract class WorkflowAction<T> extends AnnotatedDynaAction<T> {
    protected Workflow flow;

    public WorkflowAction() {
        super();
        this.flow = fetchOrCreate(actionName);
    }

    @Override
    public T call() throws Exception {
        Session session = new DefaultSession(actionName, parameters);
        Object result = flow.execute(session);
        return (T) ConvertUtils.convert(result, getReturnType());
    }

    protected Workflow fetchOrCreate(ActionName actionName) {
        WorkflowRegistration registration = WorkflowRegistration.getInstance();
        Workflow workflow = registration.get(actionName);
        if(workflow == null) {
            WorkflowBuilder builder = builder();
            build(builder);
            workflow = builder.create();
            registration.register(actionName, workflow);
        }

        return workflow;

    }

    protected abstract WorkflowBuilder builder();

    protected abstract void build(WorkflowBuilder builder);

}
