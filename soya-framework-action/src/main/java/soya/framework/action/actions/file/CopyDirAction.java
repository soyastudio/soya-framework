package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;

import java.io.File;

@ActionDefinition(
        domain = "file",
        name = "copyDir"
)
public class CopyDirAction extends DirectoryAction {
    @Override
    public File call() throws Exception {
        return null;
    }
}
