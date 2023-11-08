package soya.framework.restruts.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.NamespaceResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileResourceLoader implements NamespaceResourceLoader {

    private static final String FILE = "file:";
    public static final String[] NAMESPACES = {
            FILE
    };

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public File getResource(String name) {
        return new File(name.substring(FILE.length()));
    }

    @Override
    public <T> T getResource(String resource, Class<T> type) {
        System.out.println("===================== FileResourceInjector...");
        File file = new File(resource.substring(FILE.length()));

        if (File.class.isAssignableFrom(type)) {
            return (T) file;

        } else if (String.class.isAssignableFrom(type)) {
            try {
                return (T) IOUtils.toString(new FileInputStream(file));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
