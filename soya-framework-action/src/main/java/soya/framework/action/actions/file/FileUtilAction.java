package soya.framework.action.actions.file;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import java.io.File;
import java.util.concurrent.Callable;

public abstract class FileUtilAction<T> implements Callable<T> {

    @ActionParameterDefinition(parameterType = ActionParameterType.WIRED_PROPERTY,
            referredTo = "soya.framework.action.actions.file.base")
    protected File base;
}
