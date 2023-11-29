package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

import java.io.File;

@ActionDefinition(
        domain = "file",
        name = "listFiles"
)
public class ListFilesAction extends DirectoryAction {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.ATTRIBUTE)
    private boolean recursively;

    @Override
    public File call() throws Exception {
        return null;
    }
}
