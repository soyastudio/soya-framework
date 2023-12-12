package soya.framework.action.orchestration;

public interface Processor {
    Object process(Session session) throws Exception;
}
