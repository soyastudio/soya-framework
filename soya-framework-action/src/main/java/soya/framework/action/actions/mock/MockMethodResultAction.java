package soya.framework.action.actions.mock;

import soya.framework.action.ActionDefinition;

import java.util.concurrent.Callable;

@ActionDefinition(
        domain = "mock",
        name = "method-result"
)
public class MockMethodResultAction implements Callable<Object> {

    @Override
    public Object call() throws Exception {
        return null;
    }
}
