package soya.framework.action.orchestration;

import java.util.ArrayList;
import java.util.List;

public class PipelineBuilder implements WorkflowBuilder {
    private List<Processor> tasks = new ArrayList<>();

    public PipelineBuilder addTask(Task.Builder builder) {
        tasks.add(new TaskProcessor(builder.create()));
        return this;
    }

    public PipelineBuilder addProcessor(String name, Processor processor) {
        tasks.add(new ProcessorWrapper(name, processor));
        return this;
    }

    public PipelineBuilder addProcessor(Processor processor) {
        tasks.add(new ProcessorWrapper("", processor));
        return this;
    }

    @Override
    public Pipeline create() {
        return new Pipeline(tasks);
    }
}
