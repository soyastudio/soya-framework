package soya.framework.action.actions.dispatch;

import soya.framework.action.actions.AnnotatedDynaAction;


public abstract class DispatchAction<T> extends AnnotatedDynaAction<T> {

    public DispatchAction() {
        super();
        Dispatch dispatch = getClass().getAnnotation(Dispatch.class);
        if(dispatch == null) {
            throw new IllegalArgumentException("Dispatch annotation is required.");
        }
    }
}