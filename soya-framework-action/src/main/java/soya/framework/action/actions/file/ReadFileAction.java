package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "file",
        name = "readFile"
)
public class ReadFileAction extends FileAction<String> {

    @ActionParameterDefinition(parameterType = ActionParameterType.ATTRIBUTE)
    private String parser;
    @Override
    public String call() throws Exception {
        return null;
    }
}
