package soya.framework.restruts.io;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.DependencyInjector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResourceInjector implements DependencyInjector {

    private static final String FILE = "file:";
    private static final String[] NAMESPACES = {
            FILE
    };

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public File getWiredResource(String name) {
        return new File(name.substring(FILE.length()));
    }

    @Override
    public <T> T getWiredResource(String resource, Class<T> type) {
        System.out.println("===================== FileResourceInjector...");
        File file = new File(resource.substring(FILE.length()));

        if (File.class.isAssignableFrom(type)) {
            return (T) file;

        } else if(String.class.isAssignableFrom(type)) {
            try {
                return (T) IOUtils.toString(new FileInputStream(file));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
