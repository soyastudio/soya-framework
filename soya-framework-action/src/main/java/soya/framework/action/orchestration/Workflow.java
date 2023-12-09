package soya.framework.action.orchestration;

public interface Workflow {
    Object execute(Session session) throws Exception;
}
