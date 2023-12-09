package soya.framework.action.orchestration;

import soya.framework.commons.conversion.ConvertUtils;

public abstract class AnnotatedDispatchAction<T> extends AnnotatedDynaAction<T> {

    private Dispatcher dispatcher;

    public AnnotatedDispatchAction() {
        super();

        DispatchDefinition dispatch = getClass().getAnnotation(DispatchDefinition.class);
        if (dispatch == null) {
            throw new IllegalArgumentException("Dispatch annotation is required.");
        } else {
            this.dispatcher = Dispatchers.create(dispatch);
        }
    }

    @Override
    public T call() throws Exception {
        Object result = dispatcher.dispatch(new DefaultSession(actionName, parameters), getActionContext());
        return (T) ConvertUtils.convert(result, getReturnType());
    }

}