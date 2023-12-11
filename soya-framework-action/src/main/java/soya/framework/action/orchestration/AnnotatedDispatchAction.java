package soya.framework.action.orchestration;

import soya.framework.action.orchestration.annotation.TaskDefinition;
import soya.framework.commons.conversion.ConvertUtils;

public abstract class AnnotatedDispatchAction<T> extends AnnotatedDynaAction<T> {

    private Dispatcher dispatcher;

    public AnnotatedDispatchAction() {
        super();

        TaskDefinition dispatch = getClass().getAnnotation(TaskDefinition.class);
        if (dispatch == null) {
            throw new IllegalArgumentException("Dispatch annotation is required.");
        } else {
            this.dispatcher = Dispatchers.create(dispatch);
        }
    }

    @Override
    public T call() throws Exception {
        Object result = dispatcher.dispatch(new DefaultSession(actionName, parameters));
        return (T) ConvertUtils.convert(result, getReturnType());
    }

}