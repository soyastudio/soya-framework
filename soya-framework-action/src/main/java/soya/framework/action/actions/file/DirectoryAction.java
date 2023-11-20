package soya.framework.action.actions.file;

import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;

import java.io.File;

public abstract class DirectoryAction extends FileUtilAction<File> {

    @ActionParameter(type = ActionParameterType.PROPERTY)
    protected String dir;
}
