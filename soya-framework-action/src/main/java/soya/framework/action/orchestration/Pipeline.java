package soya.framework.action.orchestration;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipeline implements Workflow {
    private final List<Processor> tasks;

    public Pipeline(List<Processor> tasks) {
        this.tasks = Collections.unmodifiableList(tasks);
    }

    @Override
    public Object execute(Session session) throws Exception {
        Queue<Processor> taskQueue = new LinkedBlockingQueue<>(tasks);
        Object result = null;
        while (!taskQueue.isEmpty()) {
            Processor processor = taskQueue.poll();
            result = processor.process(session);
            if(processor instanceof NamedProcessor) {
                NamedProcessor namedProcessor = (NamedProcessor) processor;
                if(namedProcessor.getName() != null && !namedProcessor.getName().isEmpty()) {
                    session.set(namedProcessor.getName(), result);
                }
            }
        }

        return result;
    }

}
