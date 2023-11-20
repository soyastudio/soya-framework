package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;

import java.io.File;


@ActionDefinition(
        domain = "file",
        name = "deleteDir"
)
public class DeleteDirAction extends DirectoryAction {
    @Override
    public File call() throws Exception {
        return null;
    }
}
