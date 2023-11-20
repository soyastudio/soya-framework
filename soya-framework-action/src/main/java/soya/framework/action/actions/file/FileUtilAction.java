package soya.framework.action.actions.file;

import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;

import java.io.File;
import java.util.concurrent.Callable;

public abstract class FileUtilAction<T> implements Callable<T> {

    @ActionParameter(type = ActionParameterType.PROPERTY)
    protected File base;
}
