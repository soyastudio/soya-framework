package soya.framework.action.actions.file;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "file",
        name = "writeFile"
)
public class WriteFileAction extends FileAction<String> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.INPUT,
            required = true)
    private String content;

    @Override
    public String call() throws Exception {
        return null;
    }
}
