package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;

import java.io.File;

@ActionDefinition(
        domain = "file",
        name = "listFiles"
)
public class ListFilesAction extends DirectoryAction {

    @ActionParameter(type = ActionParameterType.PROPERTY)
    private boolean recursively;

    @Override
    public File call() throws Exception {
        return null;
    }
}
