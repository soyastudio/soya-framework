package soya.framework.action.actions.file;

import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;

public abstract class FileAction<T> extends FileUtilAction<T> {

    @ActionParameter(type = ActionParameterType.PROPERTY,
            required = true)
    protected String file;
}
