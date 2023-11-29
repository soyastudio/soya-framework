package soya.framework.action.actions.file;

import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.io.File;

public abstract class DirectoryAction extends FileUtilAction<File> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE)
    protected String dir;
}
