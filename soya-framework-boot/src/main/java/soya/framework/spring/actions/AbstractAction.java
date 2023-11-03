package soya.framework.spring.actions;

import soya.framework.restruts.ParamType;
import soya.framework.restruts.RestActionParameter;

import java.util.concurrent.Callable;

public abstract class AbstractAction<T> implements Callable<T> {
    @RestActionParameter(
            name = "message",
            paramType = ParamType.WIRED_RESOURCE,
            referredTo = "http://")
    protected String message;
}
