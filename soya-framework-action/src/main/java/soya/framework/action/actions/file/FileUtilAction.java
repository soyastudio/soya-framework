package soya.framework.action.actions.file;

import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.io.File;
import java.util.concurrent.Callable;

public abstract class FileUtilAction<T> implements Callable<T> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.WIRED_PROPERTY,
            referredTo = "soya.framework.action.actions.file.base")
    protected File base;
}
