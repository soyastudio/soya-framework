package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "file",
        name = "writeFile"
)
public class WriteFileAction extends FileAction<String> {

    @ActionParameter(type = ActionParameterType.INPUT,
            required = true)
    private String content;

    @Override
    public String call() throws Exception {
        return null;
    }
}
