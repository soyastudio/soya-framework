package soya.framework.action.flow;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionFactory;
import soya.framework.action.ActionName;

import java.util.concurrent.Callable;

public class PipelineActionFactory implements ActionFactory<PipelineAction> {

    @Override
    public Callable<?> create(ActionName actionName, ActionContext actionContext) {
        Pipeline pipeline = Pipeline.forName(actionName);
        return null;
    }
}
