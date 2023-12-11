package soya.framework.action.orchestration;

import java.util.ArrayList;
import java.util.List;

public class PipelineBuilder implements WorkflowBuilder {
    private List<Task> tasks = new ArrayList<>();

    public PipelineBuilder addTask(Task.Builder builder) {
        tasks.add(builder.create());
        return this;
    }

    @Override
    public Pipeline create() {
        return new Pipeline(tasks);
    }
}
