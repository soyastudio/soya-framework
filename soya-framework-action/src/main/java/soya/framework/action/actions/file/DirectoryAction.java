package soya.framework.action.actions.file;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import java.io.File;

public abstract class DirectoryAction extends FileUtilAction<File> {

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE)
    protected String dir;
}
