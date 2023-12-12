package soya.framework.action.orchestration;

import soya.framework.action.ActionClass;
import soya.framework.action.ActionExecutor;
import soya.framework.action.ActionName;

import java.net.URI;
import java.util.Arrays;

public final class TaskProcessor implements Processor {
    private Task task;

    public TaskProcessor(Task task) {
        this.task = task;
    }

    @Override
    public Object process(Session session) throws Exception {
        ActionName actionName = ActionName.fromURI(new URI(task.getUri()));
        ActionExecutor executor = ActionClass.forName(actionName).executor();
        Arrays.stream(task.getParameterMappings()).forEach(e -> {
            executor.set(e.getName(), session.evaluate(e.getMappingTo()));
        });

        return executor.execute();
    }
}
