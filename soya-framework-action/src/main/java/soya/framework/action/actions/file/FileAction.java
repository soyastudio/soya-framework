package soya.framework.action.actions.file;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

public abstract class FileAction<T> extends FileUtilAction<T> {

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE,
            required = true)
    protected String file;
}
