package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "file",
        name = "readFile"
)
public class ReadFileAction extends FileAction<String> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM)
    private String parser;
    @Override
    public String call() throws Exception {
        return null;
    }
}
