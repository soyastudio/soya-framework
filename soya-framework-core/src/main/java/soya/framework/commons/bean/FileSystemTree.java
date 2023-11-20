package soya.framework.commons.bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileSystemTree extends TreeBase<File> {

    public FileSystemTree(File root) throws IOException {
        super(root.getName(), root);
        String rootURI = root.toURI().toString();
        Files.walk(root.toPath())
                .forEach(path -> {
                    File file = path.toFile();
                    String uri = file.toURI().toString();
                    uri = root().getPath() + PATH_SEPARATOR + uri.substring(rootURI.length());
                    if (uri.endsWith(PATH_SEPARATOR)) {
                        uri = uri.substring(0, uri.length() - 1);
                    }
                    add(uri, file);
                });
    }
}
