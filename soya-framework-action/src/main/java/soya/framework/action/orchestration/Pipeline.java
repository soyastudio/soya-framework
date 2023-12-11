package soya.framework.action.orchestration;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipeline implements Workflow {
    private List<Task> tasks = new ArrayList<>();

    public Pipeline(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Object execute(Session session) throws Exception {
        Queue<Task> taskQueue = new LinkedBlockingQueue<>(tasks);
        boolean process = true;
        while (process && !taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            process = task.process(session);
        }
        return "Hello Pipeline";
    }

}
