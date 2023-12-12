package soya.framework.action.orchestration;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipeline implements Workflow {
    private final List<Task> tasks;

    public Pipeline(List<Task> tasks) {
        this.tasks = Collections.unmodifiableList(tasks);
    }

    @Override
    public Object execute(Session session) throws Exception {
        Queue<Task> taskQueue = new LinkedBlockingQueue<>(tasks);
        Object result = null;
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            result = new TaskProcessor(task).process(session);
            if (task.getName() != null && !task.getName().isEmpty()) {
                session.set(task.getName(), result);
            }
        }

        return result;
    }

}
