package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;

import java.io.File;

@ActionDefinition(
        domain = "file",
        name = "deleteFile"
)
public class DeleteFileAction extends FileAction<File> {
    @Override
    public File call() throws Exception {
        return null;
    }
}
