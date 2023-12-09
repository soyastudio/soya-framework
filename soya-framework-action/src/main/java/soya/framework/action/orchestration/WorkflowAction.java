package soya.framework.action.orchestration;

public abstract class WorkflowAction<T> extends AnnotatedDynaAction<T> {

    private Workflow flow;

    public WorkflowAction() {
        super();
        WorkflowRegistration registration = WorkflowRegistration.getInstance();
        this.flow = registration.get(actionName);
        if(flow == null) {
            WorkflowBuilder builder = builder();
            build(builder);
            registration.register(actionName, builder.create());
            this.flow = registration.get(actionName);
        }

    }

    @Override
    public final T call() throws Exception {
        Session session = new DefaultSession(actionName, parameters);
        return (T) flow.execute(session);
    }

    protected abstract WorkflowBuilder builder();

    protected abstract void build(WorkflowBuilder builder);

}
