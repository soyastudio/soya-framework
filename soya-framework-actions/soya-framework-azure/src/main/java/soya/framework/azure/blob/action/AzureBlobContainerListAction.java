package soya.framework.azure.blob.action;

import soya.framework.action.ActionDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "azure",
        name ="storage-blob-containers",
        description = "List all storage blob containers."
)
public class AzureBlobContainerListAction extends AzureBlobAction<String[]> {

    @Override
    public String[] call() throws Exception {
        List<String> list = new ArrayList<>();
        blobServiceClient().listBlobContainers().forEach(e -> {
            list.add(e.getName());
        });
        Collections.sort(list);

        return list.toArray(new String[list.size()]);
    }
}
