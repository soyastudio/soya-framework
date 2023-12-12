package soya.framework.action.orchestration;

import soya.framework.action.ActionClass;
import soya.framework.action.ActionExecutor;
import soya.framework.action.ActionName;

import java.util.Arrays;

public final class TaskProcessor implements NamedProcessor {
    private Task task;

    public TaskProcessor(Task task) {
        this.task = task;
    }

    @Override
    public String getName() {
        return task.getName();
    }

    @Override
    public Object process(Session session) throws Exception {
        ActionName actionName = ActionName.fromURI(task.getUri());
        ActionExecutor executor = ActionClass.forName(actionName).executor();
        Arrays.stream(task.getParameterMappings()).forEach(e -> {
            executor.set(e.getName(), session.evaluate(e.getMappingTo()));
        });

        return executor.execute();
    }
}
