package soya.framework.action.orchestration;

public class ProcessorWrapper implements NamedProcessor {
    private final String name;
    private final Processor processor;

    public ProcessorWrapper(String name, Processor processor) {
        this.name = name;
        this.processor = processor;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public Object process(Session session) throws Exception {
        return processor.process(session);
    }
}
