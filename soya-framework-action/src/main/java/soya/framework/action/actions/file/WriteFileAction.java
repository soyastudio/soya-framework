package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterDefinition;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "file",
        name = "writeFile"
)
public class WriteFileAction extends FileAction<String> {

    @ActionParameterDefinition(parameterType = ActionParameterType.INPUT,
            required = true)
    private String content;

    @Override
    public String call() throws Exception {
        return null;
    }
}
