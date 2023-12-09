package soya.framework.action.actions.file;

import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

public abstract class FileAction<T> extends FileUtilAction<T> {

    @ActionPropertyDefinition(propertyType = ActionPropertyType.PARAM,
            required = true)
    protected String file;
}
