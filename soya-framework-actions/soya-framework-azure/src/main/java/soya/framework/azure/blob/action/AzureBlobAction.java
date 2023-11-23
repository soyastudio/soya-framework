package soya.framework.azure.blob.action;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import soya.framework.context.ServiceLocatorSingleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

public abstract class AzureBlobAction<T> implements Callable<T> {

    protected BlobServiceClient blobServiceClient() {
        return ServiceLocatorSingleton.getInstance().getService(BlobServiceClient.class);
    }

    protected BlobContainerClient getBlobContainerClient(String containerName) {
        BlobContainerClient client = blobServiceClient().getBlobContainerClient(containerName);
        if (!client.exists()) {
            client.create();
        }

        return client;
    }

    protected byte[] gzip(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(data);

        return byteArrayOutputStream.toByteArray();
    }
}
