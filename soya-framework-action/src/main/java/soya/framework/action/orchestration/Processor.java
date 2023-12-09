package soya.framework.action.orchestration;

@FunctionalInterface
public interface Processor {
    void process(Session session) throws Exception;
}
