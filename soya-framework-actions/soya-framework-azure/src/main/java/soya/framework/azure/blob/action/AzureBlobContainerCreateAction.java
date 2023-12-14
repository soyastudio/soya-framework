package soya.framework.azure.blob.action;


import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionPropertyDefinition;
import soya.framework.action.ActionPropertyType;

@ActionDefinition(
        domain = "azure",
        name = "storage-blob-container-create"
)
public class AzureBlobContainerCreateAction extends AzureBlobAction<Boolean> {

    @ActionPropertyDefinition(
            propertyType = ActionPropertyType.PARAM,
            required = true)
    private String name;

    @Override
    public Boolean call() throws Exception {
        return null;
    }
}
