package soya.framework.action.actions.file;

import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

import java.io.File;

public abstract class DirectoryAction extends FileUtilAction<File> {

    @ActionParameterDefinition(type = ActionParameterType.PROPERTY)
    protected String dir;
}
